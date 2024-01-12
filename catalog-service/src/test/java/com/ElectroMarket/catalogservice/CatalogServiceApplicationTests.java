package com.ElectroMarket.catalogservice;

import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.CategoryRepository;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
@Testcontainers
public class CatalogServiceApplicationTests {
    private static KeycloakToken customerTokens;
    private static KeycloakToken employeeTokens;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Container
    private static final KeycloakContainer keycloakContainer =
            new KeycloakContainer("quay.io/keycloak/keycloak:19.0")
            .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloakContainer.getAuthServerUrl() + "realms/ElectroMarket");
    }

    @AfterEach
    void cleanup() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();
    }

    @BeforeAll
    static void generateAccessTokens() {
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() +
                        "realms/ElectroMarket/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        employeeTokens = authenticateWith("isabelle", "password", webClient);
        customerTokens = authenticateWith("bjorn", "password", webClient);
    }

    @Test
    void whenGetRequestWithIdThenProductReturned()  {
        Category category = categoryRepository.save(Category.of("category"));

        var product = new Product(1L,"Laptop", BigDecimal.valueOf(1129.99), category.id(), 10,
                "https://example.com/image.jpg", "brand", null, null, null, null, 0);

        Product expectedProduct = webTestClient
                .post()
                .uri("/v1/products")
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class).value(prod -> assertThat(prod).isNotNull())
                .returnResult().getResponseBody();

        webTestClient
                .get()
                .uri("/v1/products/" + expectedProduct.id())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Product.class).value(actualProduct -> {
                    assertThat(actualProduct).isNotNull();
                    assertThat(actualProduct.name()).isEqualTo(expectedProduct.name());
                });
    }


    @Test
    void whenPostRequestThenProductCreated()    {
        Category category = categoryRepository.save(Category.of("category"));

        var expectedProduct = new Product(1L,"Laptop", BigDecimal.valueOf(1129.99), category.id(), 10,
                "https://example.com/image.jpg", "brand", null, null, null, null, 0);

        webTestClient
                .post()
                .uri("/v1/products")
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .bodyValue(expectedProduct)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class).value(actualProduct -> {
                    assertThat(actualProduct).isNotNull();
                    assertThat(actualProduct.name()).isEqualTo(expectedProduct.name());
                });
    }

    @Test
    void whenPostRequestUnauthenticatedThen401()    {
        var expectedProduct = Product.of(
                "product",
                BigDecimal.valueOf(100.0),
                1L,
                24,
                "https://example.com/image.jpg",
                "brand");
        webTestClient
                .post()
                .uri("/v1/products")
                .bodyValue(expectedProduct)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenPostRequestUnauthorizedThen403()   {
        Category category = categoryRepository.save(Category.of("category"));
        var expectedProduct = new Product(
                1L,
                "Laptop",
                BigDecimal.valueOf(1129.99),
                category.id(),
                10,
                "https://example.com/image.jpg",
                "brand",
                null,
                null,
                null,
                null,
                0);

        webTestClient
                .post()
                .uri("/v1/products")
                .headers(headers -> headers.setBearerAuth(customerTokens.accessToken()))
                .bodyValue(expectedProduct)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPutRequestThenProductUpdated() {
        Category category = categoryRepository.save(Category.of("category"));
        var productToCreate = new Product(
                1L,
                "Laptop",
                BigDecimal.valueOf(1129.99),
                category.id(),
                10,
                "https://example.com/image.jpg",
                "brand",
                null,
                null,
                null,
                null,
                0);

        Product createdProduct = webTestClient
                .post()
                .uri("/v1/products")
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .bodyValue(productToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Product.class).value(product -> assertThat(product).isNotNull())
                .returnResult().getResponseBody();

        var productToUpdate = new Product(
                createdProduct.id(),
                createdProduct.name(),
                BigDecimal.valueOf(90.0),
                createdProduct.categoryId(),
                createdProduct.stock(),
                createdProduct.imageUrl(),
                createdProduct.brand(),
                null,
                null,
                null,
                null,
                createdProduct.version());

        webTestClient
                .put()
                .uri("/v1/products/" + productToUpdate.id())
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .bodyValue(productToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class).value(actualProduct -> {
                    assertThat(actualProduct).isNotNull();
                    assertThat(actualProduct.price()).isEqualTo(productToUpdate.price());
                });
    }

    @Test
    void whenDeleteRequestThenProductDeleted()  {
        Category category = categoryRepository.save(Category.of("category"));
        var productToCreate = new Product(
                1L,
                "Laptop",
                BigDecimal.valueOf(1129.99),
                category.id(),
                10,
                "https://example.com/image.jpg",
                "brand",
                null,
                null,
                null,
                null,
                0);

        webTestClient
                .post()
                .uri("/v1/products")
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .bodyValue(productToCreate)
                .exchange()
                .expectStatus().isCreated();

        webTestClient
                .delete()
                .uri("/v1/products/" + productToCreate.id())
                .headers(headers -> headers.setBearerAuth(employeeTokens.accessToken()))
                .exchange()
                .expectStatus().isNoContent();

        webTestClient
                .get()
                .uri("/v1/products/" + productToCreate.id())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).value(error ->
                                assertThat(error).isEqualTo("The product with id " + productToCreate.id() + " was not found."));
    }


    private static KeycloakToken authenticateWith(String username, String password, WebClient webClient)    {
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "electro-test")
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }

    private record KeycloakToken(String accessToken)    {
        @JsonCreator
        private KeycloakToken(@JsonProperty("access_token") final String accessToken)   {
            this.accessToken = accessToken;
        }
    }
}
