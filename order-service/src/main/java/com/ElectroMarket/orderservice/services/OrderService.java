package com.ElectroMarket.orderservice.services;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.dto.Product;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.ElectroMarket.orderservice.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    public OrderService(ProductClient productClient, OrderRepository orderRepository) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders()   {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(Long product_id, int quantity)   {
        return productClient.getProductById(product_id)
                .map(product -> buildAcceptedOrder(product, quantity))
                .defaultIfEmpty(
                        buildRejectedOrder(product_id, quantity)
                )
                .flatMap(orderRepository::save);
    }

    public static Order buildAcceptedOrder(Product product, int quantity)   {
        return Order.of(product.id(), product.name(), product.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(Long product_id, int quantity)   {
        return Order.of(product_id, null, null, quantity, OrderStatus.REJECTED);
    }
}
