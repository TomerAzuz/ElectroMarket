package com.ElectroMarket.mailservice.events;

public record ConfirmationSentEvent(String status, Long orderId) {}
