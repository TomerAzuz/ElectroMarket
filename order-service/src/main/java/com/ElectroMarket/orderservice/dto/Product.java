package com.ElectroMarket.orderservice.dto;

public record Product (
        Long id,
        String name,
        Double price,
        Integer stock
) {}
