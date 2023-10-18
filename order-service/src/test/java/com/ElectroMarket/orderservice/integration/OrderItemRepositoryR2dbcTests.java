package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.config.DataConfig;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.repositories.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Import(DataConfig.class)
@Testcontainers
public class OrderItemRepositoryR2dbcTests {

    @Container
    static PostgreSQLContainer<?> postgresql =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.4"));

    @Autowired
    private OrderItemRepository orderItemRepository;

    @DynamicPropertySource
    private static void postgresqlProperties(DynamicPropertyRegistry registry)  {
        registry.add("spring.r2dbc.url", OrderItemRepositoryR2dbcTests::r2dbcUrl);
        registry.add("spring.r2dbc.username", postgresql::getUsername);
        registry.add("spring.r2dbc.password", postgresql::getPassword);
        registry.add("spring.flyway.url", postgresql::getJdbcUrl);
    }

    static String r2dbcUrl()    {
        return String.format("r2dbc:postgresql://%s:%s/%s",
                postgresql.getHost(),
                postgresql.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                postgresql.getDatabaseName());
    }

    @Test
    void createItem()   {
        var item = OrderItem.of(null, 1L, 1);
        StepVerifier
                .create(orderItemRepository.save(item))
                .expectNextMatches(
                        orderItem -> orderItem.productId().equals(1L))
                .verifyComplete();
    }

    @Test
    void findItemOfNonExistingOrder()    {
        StepVerifier.create(orderItemRepository.findById(2L))
                .expectNextCount(0)
                .verifyComplete();
    }
}
