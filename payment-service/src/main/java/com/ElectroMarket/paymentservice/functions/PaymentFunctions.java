package com.ElectroMarket.paymentservice.functions;

import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import com.ElectroMarket.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class PaymentFunctions {
    private static final Logger log = LoggerFactory.getLogger(PaymentFunctions.class);

    @Bean
    Function<Flux<PaymentRequestMessage>, Flux<String>> createPayment(PaymentService paymentService) {
        return request -> request.flatMap(paymentService::createPayment);
    }

    @Bean
    Function<Flux<String>, Flux<PaymentCompletedMessage>> capturePayment(PaymentService paymentService) {
        return flux -> flux.flatMap(payload -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(payload);

                String token = jsonNode
                        .path("resource")
                        .path("id")
                        .asText();

                Long orderId = jsonNode
                        .path("resource")
                        .path("purchase_units")
                        .path(0)
                        .path("custom_id")
                        .asLong();

                return paymentService.capturePayment(token, orderId);
            } catch (Exception e) {
                log.error("Error processing payment capture: {}", e.getMessage());
                return Flux.error(e);
            }
        });
    }
}
