package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.clients.ProductClient;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.dto.Product;
import com.ElectroMarket.orderservice.event.OrderAcceptedMessage;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
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
import java.util.ArrayList;
import java.util.List;

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
        Product product = new Product(productId, "Laptop", "description.", 559.90, 5);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));

        OrderItem orderItem = OrderItem.of(null, productId, 2);
        List<OrderItem> itemList = new ArrayList<>();
        itemList.add(orderItem);
        OrderRequest orderRequest = new OrderRequest("tomer123", itemList);

        Order expectedOrder = webTestClient
                .post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(objectMapper.readValue(output.receive().getPayload(), OrderAcceptedMessage.class))
                .isEqualTo(new OrderAcceptedMessage(expectedOrder.id()));

        webTestClient.get().uri("/orders")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Order.class).value(orders ->
                        assertThat(orders.stream().filter(order -> order.id().equals(productId)).findAny()).isNotEmpty());
    }
    @Test
    void whenPostRequestAndProductNotExistsThenOrderRejected()  {
        long productId = 4;
        given(productClient.getProductById(productId)).willReturn(Mono.empty());
        OrderItem item = OrderItem.of(null, productId, 1);
        OrderRequest orderRequest = new OrderRequest("tomer123", List.of(item));

        Order expectedOrder = webTestClient.post()
                .uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).
                returnResult().
                getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }

    @Test
    void whenPostRequestAndQuantityIsGreaterThanStockThenOrderRejected()    {
        long productId = 1;
        Product product = new Product(productId, "Laptop", "description.", 559.90, 0);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));

        OrderItem orderItem = OrderItem.of(null, productId, 1);
        List<OrderItem> itemList = new ArrayList<>();
        itemList.add(orderItem);
        OrderRequest orderRequest = new OrderRequest("tomer123", itemList);

        Order expectedOrder = webTestClient.post().uri("/orders")
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class).returnResult().getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }
}
