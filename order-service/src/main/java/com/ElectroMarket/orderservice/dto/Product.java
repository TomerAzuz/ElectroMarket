package com.ElectroMarket.orderservice.dto;

public record Product (
        Long id,
        String name,
        String description,
        Double price,
        Integer stock
) {}
