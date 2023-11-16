package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.config.DataConfig;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.ElectroMarket.orderservice.repositories.OrderItemRepository;
import com.ElectroMarket.orderservice.repositories.OrderRepository;
import com.ElectroMarket.orderservice.services.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

import java.util.Objects;
import java.util.UUID;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
class OrderRepositoryR2dbcTests {
    @Container
    static PostgreSQLContainer<?> postgresql =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry)  {
        registry.add("spring.r2dbc.url", OrderRepositoryR2dbcTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
    }

    private static String r2dbcUrl()    {
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresql.getDatabaseName());
    }

    @Test
    void createRejectedOrder()  {
        var rejectedOrder = OrderService.buildOrder( 0.0, false);
        StepVerifier
                .create(orderRepository.save(rejectedOrder))
                .expectNextMatches(
                        order -> order.status().equals(OrderStatus.REJECTED))
                .verifyComplete();
    }

    @Test
    void findOrderByIdWhenNotExisting() {
        StepVerifier.create(orderRepository.findById(1234L))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void whenCreateOrderNotAuthenticatedThenNoAuditMetadata() {
        var rejectedOrder = OrderService.buildOrder( 1000.0, false);
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> Objects.isNull(order.createdBy()) &&
                        Objects.isNull(order.lastModifiedBy()))
                .verifyComplete();
    }

    @Test
    @WithMockUser("tomer")
    void whenCreateOrderAuthenticatedThenAuditMetadata() {
        var rejectedOrder = OrderService.buildOrder( 1000.0, false);
        StepVerifier.create(orderRepository.save(rejectedOrder))
                .expectNextMatches(order -> order.createdBy().equals("tomer") &&
                        order.lastModifiedBy().equals("tomer"))
                .verifyComplete();
    }
}
