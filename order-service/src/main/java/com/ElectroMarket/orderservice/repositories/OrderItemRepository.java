package com.ElectroMarket.orderservice.repositories;

import com.ElectroMarket.orderservice.models.OrderItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface OrderItemRepository extends ReactiveCrudRepository<OrderItem,Long> {
    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    Flux<OrderItem> getItemsForOrder(@Param("orderId") Long orderId);
}
