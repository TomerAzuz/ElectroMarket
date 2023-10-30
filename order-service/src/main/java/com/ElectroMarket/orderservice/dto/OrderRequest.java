package com.ElectroMarket.orderservice.dto;

import com.ElectroMarket.orderservice.models.OrderItem;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest (
        @NotNull
        String username,
        @NotNull
        List<OrderItem> items
) {}
