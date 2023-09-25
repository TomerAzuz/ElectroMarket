package com.ElectroMarket.orderservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;
import java.time.Duration;

@ConfigurationProperties(prefix = "electro")
public record ClientProperties(
        @NotNull
        URI catalogServiceUri,
        Duration requestTimeout
) {}
