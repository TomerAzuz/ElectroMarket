package com.ElectroMarket.orderservice.repositories;

import com.ElectroMarket.orderservice.models.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order,Long> {}
