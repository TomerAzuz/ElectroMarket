package com.ElectroMarket.orderservice.event;

import com.ElectroMarket.orderservice.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Configuration
public class OrderFunctions {
    private static final Logger log = LoggerFactory.getLogger(OrderFunctions.class);

    @Bean
    public Consumer<Flux<PaymentCompletedEvent>> paymentCompleted(OrderService orderService) {
        return message -> orderService.consumePaymentCompletedEvent(message)
                        .doOnNext(order -> log.info("Payment status for order with id {}: {}", order.id() ,order.status()))
                        .subscribe();
    }

    @Bean
    public Consumer<Flux<ConfirmationSentEvent>> confirmationSent(OrderService orderService) {
        return message -> orderService.consumeConfirmationSentEvent(message)
                .doOnNext(order -> log.info("Confirmation sent for order with id {}\n status: {}", order.id(), order.status()))
                .subscribe();
    }
}