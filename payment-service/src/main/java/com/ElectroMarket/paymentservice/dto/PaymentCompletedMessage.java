package com.ElectroMarket.paymentservice.dto;

public record PaymentCompletedMessage(String status, Long orderId) {}
