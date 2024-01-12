package com.ElectroMarket.paymentservice.dto;

import java.math.BigDecimal;

public record PaymentRequestMessage(Long orderId, BigDecimal total) {}
