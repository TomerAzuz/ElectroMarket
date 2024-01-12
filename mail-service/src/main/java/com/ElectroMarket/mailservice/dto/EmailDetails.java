package com.ElectroMarket.mailservice.dto;

public record EmailDetails(String recipient, String subject, String message, String attachment) {}
