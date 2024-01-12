package com.ElectroMarket.paymentservice;

import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import com.ElectroMarket.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;


@FunctionalSpringBootTest
public class PaymentFunctionsIntegrationTests {

    @Autowired
    private FunctionCatalog catalog;

    @MockBean
    private PaymentService paymentService;

    @Test
    void createPayment()    {
        Function<Flux<PaymentRequestMessage>, Flux<String>> createPayment = catalog.lookup(Function.class, "createPayment");
        var paymentRequest = new PaymentRequestMessage(123L, BigDecimal.ONE);
        var requestFlux = Flux.just(paymentRequest);
        given(paymentService.createPayment(paymentRequest)).willReturn(Flux.just("redirectUrl"));
        StepVerifier.create(createPayment.apply(requestFlux))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    void capturePayment() {
        Function<Flux<String>, Flux<PaymentCompletedMessage>> capturePayment = catalog.lookup(Function.class, "capturePayment");

        var payload = "{\"resource\": {\"id\": \"valid_token\", \"purchase_units\": [{\"custom_id\": 123}]}}";
        var paymentCompletedMessage = new PaymentCompletedMessage("success", 123L);

        given(paymentService.capturePayment(eq("valid_token"), eq(123L)))
                .willReturn(Flux.just(paymentCompletedMessage));

        StepVerifier.create(capturePayment.apply(Flux.just(payload)))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }
}
