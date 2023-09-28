package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.clients.ProductClient;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.dto.Product;
import com.ElectroMarket.orderservice.event.OrderAcceptedMessage;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestChannelBinderConfiguration.class)
@Testcontainers
public class OrderServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    OutputDestination output;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ProductClient productClient;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry)  {
        registry.add("spring.r2dbc.url", OrderServiceApplicationTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
    }

    private static String r2dbcUrl()    {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgresql.getDatabaseName());
    }

    @Test
    void whenGetOrdersThenReturn() throws IOException    {
        long productId = 1;
        Product product = new Product(productId, "Laptop", "Some description1.", 559.90, 5);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));
        OrderRequest orderRequest = new OrderRequest(productId, 1);
        Order expectedOrder = webTestClient.post().uri("/api/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();
        assertThat(expectedOrder).isNotNull();
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
                .isEqualTo(new OrderAcceptedMessage(expectedOrder.id()));

        webTestClient.get().uri("/api/orders")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Order.class).value(orders -> {
                    assertThat(orders.stream().filter(order -> order.id().equals(productId)).findAny()).isNotEmpty();
                });
    }

    @Test
    void whenPostRequestAndProductExistsThenOrderAccepted() throws IOException  {
        long productId = 1;
        Product product = new Product(productId, "Laptop", "Some description1.", 559.90, 5);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));
        OrderRequest orderRequest = new OrderRequest(productId, 5);

        Order createdOrder = webTestClient.post().uri("/api/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.id()).isEqualTo(orderRequest.product_id());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.productName()).isEqualTo(product.name());
        assertThat(createdOrder.productPrice()).isEqualTo(product.price());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.ACCEPTED);

        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
                .isEqualTo(new OrderAcceptedMessage(createdOrder.id()));
    }

    @Test
    void whenPostRequestAndProductNotExistsThenOrderRejected()  {
        long productId = 4;
        given(productClient.getProductById(productId)).willReturn(Mono.empty());
        OrderRequest orderRequest = new OrderRequest(productId, 3);

        Order createdOrder = webTestClient.post().uri("/api/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.productId()).isEqualTo(orderRequest.product_id());
        assertThat(createdOrder.quantity()).isEqualTo(orderRequest.quantity());
        assertThat(createdOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }
}
