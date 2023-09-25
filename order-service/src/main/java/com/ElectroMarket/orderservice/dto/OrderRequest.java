package com.ElectroMarket.orderservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest (
        @NotNull
        Long product_id,
        @NotNull(message = "The product quantity is required.")
        @Min(value = 1, message = "You must order at least 1 item.")
        @Max(value = 10, message = "You cannot order more than 10 items.")
        Integer quantity
) {}
