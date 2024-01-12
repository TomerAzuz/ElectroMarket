package com.ElectroMarket.mailservice.events;

public record ConfirmationMailRequest(Long orderId, String userEmail) {}
