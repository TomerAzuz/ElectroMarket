package com.ElectroMarket.orderservice.repositories;

import com.ElectroMarket.orderservice.models.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {
    Flux<Order> findAllByCreatedBy(String userId);
}
