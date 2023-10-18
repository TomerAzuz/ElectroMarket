package com.ElectroMarket.orderservice.controllers;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Flux<Order> getAllOrders()   {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest)   {
        return orderService.submitOrder(orderRequest);
    }
}
