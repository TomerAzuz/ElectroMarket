package com.ElectroMarket.catalogservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public record Category (
        @Id
        Long id,
        @NotBlank(message = "The category name is required.")
        @Size(max = 100, message = "The category name cannot exceed 100 characters.")
        String name
) {
        public static Category of(String name)   {
                return new Category(null, name);
        }
}
