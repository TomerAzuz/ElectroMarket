package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.dto.Product;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.models.OrderStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class OrderServiceApplicationTests {

    private static KeycloakToken customerTokens;

    @Container
    private static final KeycloakContainer keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
			.withRealmImportFile("test-realm-config.json");
    @Container
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

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

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/ElectroMarket");
    }

    private static String r2dbcUrl()    {
        return String.format("r2dbc:postgresql://%s:%s/%s", postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT), postgresql.getDatabaseName());
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "realms/ElectroMarket/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        customerTokens = authenticateWith("bjorn", "password", webClient);
    }

    @Test
    void whenGetOrdersThenReturn() throws IOException    {
        long productId = 1;
        Product product = new Product(productId, "Laptop", BigDecimal.valueOf(559.90), 5);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));

        OrderItem orderItem = OrderItem.of(null, productId, 2);
        List<OrderItem> orderRequest = List.of(orderItem);

        Order expectedOrder = webTestClient
                .post()
                .uri("/v1/orders")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.PAYMENT_PENDING);

        webTestClient
                .get()
                .uri("/v1/orders")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Order.class).value(orders ->
                        assertThat(orders.stream().filter(order -> order.id().equals(productId)).findAny()).isNotEmpty());
    }

    @Test
    void whenPostRequestThenOrderAccepted() throws IOException {
        long productId = 1;
        Product product = new Product(productId, "Laptop", BigDecimal.valueOf(559.90), 5);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));

        OrderItem orderItem = OrderItem.of(null, productId, 2);
        List<OrderItem> orderRequest = List.of(orderItem);

        Order expectedOrder = webTestClient
                .post()
                .uri("/v1/orders")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.PAYMENT_PENDING);
    }

    @Test
    void whenPostRequestAndProductNotExistsThenOrderRejected()  {
        long productId = 4;
        given(productClient.getProductById(productId)).willReturn(Mono.empty());
        OrderItem item = OrderItem.of(null, productId, 1);
        List<OrderItem> orderRequest = List.of(item);

        Order expectedOrder = webTestClient
                .post()
                .uri("/v1/orders")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }

    @Test
    void whenPostRequestAndQuantityIsGreaterThanStockThenOrderRejected()    {
        long productId = 1;
        Product product = new Product(productId, "Laptop", BigDecimal.valueOf(559.90), 0);
        given(productClient.getProductById(productId)).willReturn(Mono.just(product));

        OrderItem orderItem = OrderItem.of(null, productId, 1);
        List<OrderItem> orderRequest = List.of(orderItem);

        Order expectedOrder = webTestClient
                .post()
                .uri("/v1/orders")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .bodyValue(orderRequest)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertThat(expectedOrder).isNotNull();
        assertThat(expectedOrder.status()).isEqualTo(OrderStatus.REJECTED);
    }

    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient) {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "electro-test")
                        .with("username", username)
                        .with("password", password)
                )
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String accessToken) {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
