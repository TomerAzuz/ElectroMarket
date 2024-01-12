package com.ElectroMarket.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
@EnableR2dbcAuditing
public class DataConfig {
    @Bean
    ReactiveAuditorAware<String> auditorAware() {
        return () -> ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(authentication -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof Jwt) {
                        return ((Jwt) principal).getClaimAsString("email");
                    } else {
                        return authentication.getName();
                    }
                });
    }
}
