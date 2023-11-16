package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.config.SecurityConfig;
import com.ElectroMarket.orderservice.controllers.OrderController;
import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.ElectroMarket.orderservice.services.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class)
@Import(SecurityConfig.class)
public class OrderControllerWebFluxTests {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    ReactiveJwtDecoder reactiveJwtDecoder;

    @Test
    void whenProductNotAvailableThenRejectOrder()   {
        var item = OrderItem.of(null, 1000L, 1);
        var orderRequest = new OrderRequest( List.of(item));
        var expectedOrder = OrderService.buildOrder( 0.0, false);
        given(orderService.submitOrder(orderRequest))
                .willReturn(Mono.just(expectedOrder));

        webClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer")))
            .post()
            .uri("/orders")
            .bodyValue(orderRequest)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).value(actualOrder -> {
                assertThat(actualOrder).isNotNull();
                assertThat(actualOrder.status()).isEqualTo(OrderStatus.REJECTED);
            });
    }

    @Test
    void getExistingOrder()    {
        var order = new Order(1L, "tomer123", 1000.0,
                OrderStatus.ACCEPTED, null, null, null,0);

        given(orderService.getOrderById(1L))
                .willReturn(Mono.just(order));

        webClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer")))
            .get()
            .uri("/orders/{id}", order.id())
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody(Order.class).value(actualOrder -> {
                assertThat(actualOrder).isNotNull();
                assertThat(actualOrder.id()).isEqualTo(order.id());
                assertThat(actualOrder.status()).isEqualTo(order.status());
            });
    }

    @Test
    void getOrderItems()    {
        var item1 = OrderItem.of(1L, 1L, 1);
        var item2 = OrderItem.of(1L, 2L, 1);
        var item3 = OrderItem.of(1L, 3L, 1);

        given(orderService.getItemsForOrder(1L))
                .willReturn(Flux.fromIterable(List.of(item1, item2, item3)));

        webClient
                .mutateWith(SecurityMockServerConfigurers.mockJwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_customer")))
            .get()
            .uri("/orders/{id}/items", 1L)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBodyList(OrderItem.class)
            .value(actualItems -> {
                assertThat(actualItems).hasSize(3);
                assertThat(actualItems).contains(item1, item2, item3);
            });
    }
}
