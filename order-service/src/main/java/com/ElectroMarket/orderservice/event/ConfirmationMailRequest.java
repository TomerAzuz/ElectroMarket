package com.ElectroMarket.orderservice.event;

public record ConfirmationMailRequest(Long orderId, String userEmail) {}
