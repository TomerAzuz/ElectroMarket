package com.ElectroMarket.orderservice.services;

import com.ElectroMarket.orderservice.clients.ProductClient;
import com.ElectroMarket.orderservice.event.ConfirmationMailRequest;
import com.ElectroMarket.orderservice.event.ConfirmationSentEvent;
import com.ElectroMarket.orderservice.event.PaymentCompletedEvent;
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

import java.math.BigDecimal;
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

    public Mono<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Flux<OrderItem> getItemsForOrder(Long id) {
        return orderItemRepository.getItemsForOrder(id);
    }

    @Transactional
    public Mono<Order> submitOrder(List<OrderItem> orderRequest) {
        return Flux.fromIterable(orderRequest)
                .flatMap(this::validateProduct)
                .collectList()
                .map(isValidProductList -> isValidProductList.stream().allMatch(Boolean::booleanValue))
                .flatMap(isAccepted -> calculateAndSaveOrder(orderRequest, isAccepted));
    }

    private Mono<Boolean> validateProduct(OrderItem item) {
        /* Check product existence and stock level */
        return productClient.getProductById(item.productId())
                .map(product -> product != null && product.stock() >= item.quantity())
                .defaultIfEmpty(false);
    }

    private Mono<Order> calculateAndSaveOrder(List<OrderItem> orderRequest, boolean isAccepted) {
        /* Ensure unique products */
        Set<Long> uniqueProductIds = orderRequest.stream()
                .map(OrderItem::productId)
                .collect(Collectors.toSet());

        /* Calculate total price */
        Mono<Map<Long, BigDecimal>> productPrices = Flux.fromIterable(uniqueProductIds)
                .flatMap(productId -> productClient.getProductById(productId)
                        .map(product -> Map.entry(productId, product.price())))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);

        return productPrices.flatMap(prices -> {
            BigDecimal total = orderRequest.stream()
                    .map(item -> {
                        BigDecimal price = prices.getOrDefault(item.productId(), BigDecimal.ZERO);
                        return price.multiply(BigDecimal.valueOf(item.quantity()));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return buildAndSaveOrder(orderRequest, total, isAccepted);
        });
    }

    private Mono<Order> buildAndSaveOrder(List<OrderItem> orderRequest, BigDecimal total, boolean isAccepted) {
        Order order = buildOrder(total, isAccepted);
        return Mono.from(orderRepository.save(order))
                .flatMap(savedOrder -> saveOrderItems(savedOrder, orderRequest));
    }

    public static Order buildOrder(BigDecimal totalPrice, boolean isAccepted) {
        return isAccepted ? Order.of(totalPrice, OrderStatus.PAYMENT_PENDING) :
                Order.of(BigDecimal.ZERO, OrderStatus.REJECTED);
    }

    private Mono<Order> saveOrderItems(Order order, List<OrderItem> items) {
        Long orderId = order.id();
        List<OrderItem> orderItems = items.stream()
                .map(item -> OrderItem.of(orderId, item.productId(), item.quantity()))
                .collect(Collectors.toList());

        return orderItemRepository.saveAll(orderItems).collectList().map(savedOrderItems -> order);
    }

    public Flux<Order> consumePaymentCompletedEvent(Flux<PaymentCompletedEvent> message) {
        return message
                .flatMap(paymentStatusMessage -> orderRepository.findById(paymentStatusMessage.orderId())
                .filter(existingOrder -> OrderStatus.PAYMENT_PENDING.equals(existingOrder.status()))
                .map(existingOrder -> {
                    OrderStatus paymentStatus = "COMPLETED".equals(paymentStatusMessage.status())
                            ? OrderStatus.PAYMENT_COMPLETED
                            : OrderStatus.PAYMENT_CANCELLED;

                    if (paymentStatus.equals(OrderStatus.PAYMENT_COMPLETED))    {
                        publishConfirmationMailRequest(existingOrder.id(), existingOrder.createdBy());
                    }
                    return buildOrderWithStatus(existingOrder, paymentStatus);
                })
                .flatMap(orderRepository::save));
    }

    public Flux<Order> consumeConfirmationSentEvent(Flux<ConfirmationSentEvent> message) {
        return message
                .flatMap(event -> orderRepository.findById(event.orderId()))
                .filter(existingOrder -> OrderStatus.PAYMENT_COMPLETED.equals(existingOrder.status()))
                .map(existingOrder -> buildOrderWithStatus(existingOrder, OrderStatus.CONFIRMATION_SENT))
                .flatMap(orderRepository::save);
    }

    private void publishConfirmationMailRequest(Long orderId, String userEmail)   {
        var request = new ConfirmationMailRequest(orderId, userEmail);
        log.info("Sending confirmation mail request for order id {}", orderId);
        var result = streamBridge.send("confirmationRequest-out-0", request);
        log.info("Result of sending confirmation mail request: {}", result);
    }

    private Order buildOrderWithStatus(Order existingOrder, OrderStatus newOrderStatus) {
        return new Order(
                existingOrder.id(),
                existingOrder.createdBy(),
                existingOrder.total(),
                newOrderStatus,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.lastModifiedBy(),
                existingOrder.version()
        );
    }
}