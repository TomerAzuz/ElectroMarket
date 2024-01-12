package com.ElectroMarket.paymentservice;

import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;

@FunctionalSpringBootTest
public class PaymentFunctionsIntegrationTests {

    @Autowired
    private FunctionCatalog catalog;

    @Test
    void createPayment()    {
        Function<Flux<PaymentRequestMessage>, Flux<String>> createPayment = catalog.lookup(Function.class, "createPayment");
        var request = Flux.just(new PaymentRequestMessage(123L, BigDecimal.ONE));
        StepVerifier.create(createPayment.apply(request))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

    @Test
    void capturePayment() {
        Function<Flux<String>, Flux<PaymentCompletedMessage>> capturePayment = catalog.lookup(Function.class, "capturePayment");

        var jsonPayload = "{\"resource\": {\"id\": \"valid_token\", \"purchase_units\": [{\"custom_id\": 123}]}}";
        var token = Flux.just(jsonPayload);

        StepVerifier.create(capturePayment.apply(token))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }
}
