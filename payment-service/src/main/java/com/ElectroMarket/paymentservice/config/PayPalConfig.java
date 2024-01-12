package com.ElectroMarket.paymentservice.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "paypal")
public class PayPalConfig {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Bean
    public PayPalHttpClient getPayPalClient() {
        return new PayPalHttpClient(new PayPalEnvironment.Sandbox(clientId, clientSecret));
    }
}
