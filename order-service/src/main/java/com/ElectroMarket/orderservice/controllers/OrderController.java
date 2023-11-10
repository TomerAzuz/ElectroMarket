package com.ElectroMarket.orderservice.controllers;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
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

    @GetMapping("{id}")
    public Mono<Order> getOrderById(@PathVariable Long id)   {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Mono<Order> submitOrder(@RequestBody @Valid OrderRequest orderRequest)   {
        return orderService.submitOrder(orderRequest);
    }

    @GetMapping("{id}/items")
    Flux<OrderItem> getItemsForOrder(@PathVariable Long id) {
        return orderService.getItemsForOrder(id);
    }

    @GetMapping("user/{username}")
    Flux<Order> getOrdersOfUser(@PathVariable("username") String username)  {
        return orderService.getOrdersByUsername(username);
    }
}
