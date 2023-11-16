package com.ElectroMarket.orderservice.controllers;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders(@AuthenticationPrincipal Jwt jwt)   {
        log.info("Fetching all orders");
        return orderService.getAllOrders(jwt.getSubject());
    }

    @GetMapping("{id}")
    public Mono<Order> getOrderById(@PathVariable Long id)   {
        log.info("Fetching order with id {}", id);
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest)   {
        log.info("New order received");
        return orderService.submitOrder(orderRequest);
    }

    @GetMapping("{id}/items")
    Flux<OrderItem> getItemsForOrder(@PathVariable Long id) {
        log.info("Fetching items for order with id {}", id);
        return orderService.getItemsForOrder(id);
    }

}
