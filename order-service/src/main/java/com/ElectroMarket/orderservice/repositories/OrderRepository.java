package com.ElectroMarket.orderservice.repositories;

import com.ElectroMarket.orderservice.models.Order;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
    @Query("SELECT * FROM orders WHERE username = :username")
    Flux<Order> findOrdersByUsername(@Param("userId") String username);
}
