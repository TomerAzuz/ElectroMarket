package com.ElectroMarket.orderservice.services;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.dto.OrderRequest;
import com.ElectroMarket.orderservice.event.OrderAcceptedMessage;
import com.ElectroMarket.orderservice.event.OrderDispatchedMessage;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.ElectroMarket.orderservice.repositories.OrderItemRepository;
import com.ElectroMarket.orderservice.repositories.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StreamBridge streamBridge;

    public OrderService(ProductClient productClient, OrderRepository orderRepository,
                        StreamBridge streamBridge, OrderItemRepository orderItemRepository) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public Mono<Order> submitOrder(OrderRequest orderRequest) {
        return Flux.fromIterable(orderRequest.items())
                .flatMap(item -> productClient.getProductById(item.productId())
                        .flatMap(product -> {
                            boolean isAccepted = product != null && product.stock() >= item.quantity();
                            return Mono.just(isAccepted);
                        })
                        .defaultIfEmpty(false) // Set to false when product is not found
                )
                .collectList()
                .flatMap(acceptedList -> {
                    boolean isAccepted = acceptedList.stream().allMatch(Boolean::booleanValue);
                    return calculateAndSaveOrder(orderRequest, isAccepted);
                });
    }

    private Mono<Order> calculateAndSaveOrder(OrderRequest orderRequest, boolean isAccepted) {
        return Flux.fromIterable(orderRequest.items())
                .flatMap(item -> productClient.getProductById(item.productId())
                        .map(product -> product.price() * item.quantity()))
                .reduce(0.0, Double::sum)
                .flatMap(total -> {
                    Order order = buildOrder(total, isAccepted);
                    return Mono.from(orderRepository.save(order))
                            .flatMap(savedOrder -> saveOrderItems(savedOrder, orderRequest.items()))
                            .doOnSuccess(this::publishOrderAcceptedEvent);
                });
    }

    private Mono<Order> saveOrderItems(Order order, List<OrderItem> items) {
        Long orderId = order.id();
        List<OrderItem> orderItems = items.stream()
                .map(item -> OrderItem.of(orderId, item.productId(), item.quantity()))
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(orderItems).collectList().map(savedOrderItems -> order);
    }

    public static Order buildOrder(Double totalPrice, boolean isAccepted) {
        return isAccepted ? Order.of(totalPrice, OrderStatus.ACCEPTED) : Order.of(0.0, OrderStatus.REJECTED);
    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux
                .flatMap(message -> orderRepository.findById(message.orderId()))
                .filter(existingOrder -> !OrderStatus.DISPATCHED.equals(existingOrder.status()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return new Order(
                existingOrder.id(),
                existingOrder.total(),
                OrderStatus.DISPATCHED,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.version()
        );
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED)) {
            return;
        }
        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Sending order accepted event with id {}", order.id());
        var result = streamBridge.send("acceptedOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}", order.id(), result);
    }
}
