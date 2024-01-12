package com.ElectroMarket.edgeservice.models;

import java.util.List;

public record User(
        String username,
        String firstName,
        String lastName,
        String email,
        List<String> roles
) {}
