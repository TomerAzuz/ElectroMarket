package com.ElectroMarket.orderservice.event;

public record ConfirmationSentEvent (String status, Long orderId) {}
