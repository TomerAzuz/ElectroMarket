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
import java.util.Map;
import java.util.Set;
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

    public Flux<Order> getAllOrders(String userId) {
        return orderRepository.findAllByCreatedBy(userId);
    }

    public Mono<Order> getOrderById(Long id)   {
        return orderRepository.findById(id);
    }

    public Flux<OrderItem> getItemsForOrder(Long id)    {
        return orderItemRepository.getItemsForOrder(id);
    }

    @Transactional
    public Mono<Order> submitOrder(OrderRequest orderRequest) {
        return Flux.fromIterable(orderRequest.items())
                .flatMap(this::verifyProduct)
                .collectList()
                .map(isValidProductList -> isValidProductList.stream().allMatch(Boolean::booleanValue))
                .flatMap(isAccepted -> calculateAndSaveOrder(orderRequest, isAccepted));
    }


    private Mono<Boolean> verifyProduct(OrderItem item) {
        return productClient.getProductById(item.productId())
                .map(product -> product != null && product.stock() >= item.quantity())
                .defaultIfEmpty(false);
    }

    private Mono<Order> calculateAndSaveOrder(OrderRequest orderRequest, boolean isAccepted) {
        Set<Long> uniqueProductIds = orderRequest.items().stream()
                .map(OrderItem::productId)
                .collect(Collectors.toSet());

        // FIXME
        Mono<Map<Long, Double>> productPrices = Flux.fromIterable(uniqueProductIds)
                .flatMap(productId -> productClient.getProductById(productId)
                        .map(product -> Map.entry(productId, product.price())))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);

        return productPrices.flatMap(prices -> {
            double total = orderRequest.items().stream()
                    .mapToDouble(item -> prices.getOrDefault(item.productId(), 0.0) * item.quantity())
                    .sum();
            return buildAndSaveOrder(orderRequest, total, isAccepted);
        });
    }

    private Mono<Order> buildAndSaveOrder(OrderRequest orderRequest, double total, boolean isAccepted) {
        Order order = buildOrder(total, isAccepted);
        return Mono.from(orderRepository.save(order))
                .flatMap(savedOrder -> saveOrderItems(savedOrder, orderRequest.items()))
                .doOnSuccess(this::publishOrderAcceptedEvent);
    }

    public static Order buildOrder(Double totalPrice, boolean isAccepted) {
        return isAccepted ? Order.of(totalPrice, OrderStatus.ACCEPTED) :
                Order.of(0.0, OrderStatus.REJECTED);
    }

    private Mono<Order> saveOrderItems(Order order, List<OrderItem> items) {
        Long orderId = order.id();
        List<OrderItem> orderItems = items.stream()
                .map(item -> OrderItem.of(orderId, item.productId(), item.quantity()))
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(orderItems).collectList().map(savedOrderItems -> order);
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
                existingOrder.createdBy(),
                existingOrder.total(),
                OrderStatus.DISPATCHED,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.lastModifiedBy(),
                existingOrder.version()
        );
    }

    private void publishOrderAcceptedEvent(Order order) {
        log.info("Publishing order accepted event...");
        if (!order.status().equals(OrderStatus.ACCEPTED)) {
            return;
        }
        OrderAcceptedMessage orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.debug("OrderAcceptedMessage content: {}", orderAcceptedMessage);
        log.info("Sending order accepted event with id {}", order.id());
        var result = streamBridge.send("acceptedOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}", order.id(), result);
    }
}