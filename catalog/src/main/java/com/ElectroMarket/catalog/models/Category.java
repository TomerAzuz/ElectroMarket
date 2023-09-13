package com.ElectroMarket.catalog.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public record Category (
        @Id
        Long id,
        @NotBlank(message = "The category name is required.")
        @Size(max = 100, message = "The category name cannot exceed 100 characters.")
        String name,
        Long parent_id
) {
        public static Category of(String name, Long parent_id)   {
                return new Category(null, name, parent_id);
        }
}
