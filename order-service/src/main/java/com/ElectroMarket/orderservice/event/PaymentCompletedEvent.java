package com.ElectroMarket.orderservice.event;

public record PaymentCompletedEvent(String status, Long orderId) {}
