package com.ElectroMarket.paymentservice.service;

import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final StreamBridge streamBridge;

    @Autowired
    private PayPalHttpClient payPalClient;

    public PaymentService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public Flux<String> createPayment(PaymentRequestMessage request) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown()
                .currencyCode("USD").value(request.total().toString());
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown)
                .customId(request.orderId().toString());

        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:9000/order-confirmed")
                .cancelUrl("http://localhost:9000/order-cancelled");
        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);

        return Mono.fromCallable(() -> {
            HttpResponse<Order> orderResponse = payPalClient.execute(ordersCreateRequest);
            Order order = orderResponse.result();
            
            return order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .href();
        }).flatMapMany(Flux::just)
            .onErrorResume(e -> {
                log.error("Error creating payment for order id " + request.orderId() + ": " + e.getMessage());
                return Flux.error(e);
        });
    }

    public Flux<PaymentCompletedMessage> capturePayment(String token, Long orderId) {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        return Flux.defer(() ->
                Mono.fromCallable(() -> {
                    HttpResponse<Order> httpResponse = payPalClient.execute(ordersCaptureRequest);
                    String status = httpResponse.result().status();
                    PaymentCompletedMessage response = new PaymentCompletedMessage(status, orderId);
                    publishPaymentCapturedEvent(response);
                    return response;
                })
        ).onErrorResume(e -> {
            log.error("Error capturing payment for order ID " + orderId + ": " + e.getMessage());
            return cancelPayment(orderId);
        });
    }

    private Flux<PaymentCompletedMessage> cancelPayment(Long orderId) {
        PaymentCompletedMessage response = new PaymentCompletedMessage("CANCELLED", orderId);
        publishPaymentCapturedEvent(response);
        return Flux.just(response);
    }

    public void publishPaymentCapturedEvent(PaymentCompletedMessage paymentCompletedMessage) {
        log.info("Sending payment completed event for order ID: {}, status: {}", paymentCompletedMessage.orderId(), paymentCompletedMessage.status());
        var result = streamBridge.send("paymentCompleted-out-0", paymentCompletedMessage);
        log.info("Result of sending data for order with ID {}: {}", paymentCompletedMessage.orderId(), result);
    }
}
