package com.ElectroMarket.orderservice.clients;

import com.ElectroMarket.orderservice.dto.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class ProductClient {
    private static final String PRODUCTS_ROOT_API = "/v1/products/";
    private final WebClient webClient;
    public ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }
    public Mono<Product> getProductById(Long id)    {
        return webClient
                .get()
                .uri(PRODUCTS_ROOT_API + id.toString())
                .retrieve()
                .bodyToMono(Product.class)
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
                .onErrorResume(Exception.class, exception -> Mono.empty());
    }
}
