package com.ElectroMarket.orderservice.services;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.dto.Product;
import com.ElectroMarket.orderservice.event.OrderAcceptedMessage;
import com.ElectroMarket.orderservice.event.OrderDispatchedMessage;
import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderStatus;
import com.ElectroMarket.orderservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final Logger log =
            LoggerFactory.getLogger(OrderService.class);
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public OrderService(ProductClient productClient, OrderRepository orderRepository,
                        StreamBridge streamBridge) {
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getAllOrders()   {
        return orderRepository.findAll();
    }

    @Transactional
    public Mono<Order> submitOrder(Long product_id, int quantity)   {
        return productClient.getProductById(product_id)
                .map(product -> buildAcceptedOrder(product, quantity))
                .defaultIfEmpty(
                        buildRejectedOrder(product_id, quantity)
                )
                .flatMap(orderRepository::save)
                .doOnNext(this::publishOrderAcceptedEvent);
    }

    public static Order buildAcceptedOrder(Product product, int quantity)   {
        return Order.of(product.id(), product.name(), product.price(), quantity, OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(Long product_id, int quantity)   {
        return Order.of(product_id, null, null, quantity, OrderStatus.REJECTED);
    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux)   {
        return flux
                .flatMap(message ->
                        orderRepository.findById(message.orderId()))
                .filter(existingOrder -> !OrderStatus.DISPATCHED.equals(existingOrder.status()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return new Order(
                existingOrder.id(),
                existingOrder.productId(),
                existingOrder.productName(),
                existingOrder.productPrice(),
                existingOrder.quantity(),
                OrderStatus.DISPATCHED,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.version()
        );
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.status().equals(OrderStatus.ACCEPTED))   {
            return;
        }
        var orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Sending order accepted event with id {} ", order.id());
        var result = streamBridge.send("acceptedOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}", order.id(), result);
    }
}
