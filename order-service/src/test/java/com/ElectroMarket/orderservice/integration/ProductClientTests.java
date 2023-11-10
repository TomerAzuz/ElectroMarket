package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.dto.Product;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;


@TestMethodOrder(MethodOrderer.Random.class)
class ProductClientTests {
    private MockWebServer mockWebServer;
    private ProductClient productClient;

    @BeforeEach
    void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        var webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").uri().toString()).build();
        this.productClient = new ProductClient(webClient);
    }

    @AfterEach
    void clean() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void whenProductExistsThenReturnProduct()   {
        var productId = 1L;
        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                            {
                                "id": %d,
                                "name": "Product name",
                                "price": 9.99
                            }
                        """.formatted(productId));
        mockWebServer.enqueue(mockResponse);

        Mono<Product> product = productClient.getProductById(productId);

        StepVerifier.create(product)
                .expectNextMatches(p -> p.id().equals(productId))
                .verifyComplete();
    }

    @Test
    void whenProductNotExistsThenReturnEmpty()  {
        var productId = 1L;

        var mockResponse = new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(404);

        mockWebServer.enqueue(mockResponse);

        StepVerifier.create(productClient.getProductById(productId))
                .expectNextCount(0)
                .verifyComplete();
    }
}
