package com.ElectroMarket.catalogservice.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

public record Category (
        @Id
        Long id,

        @Column("name")
        @NotBlank(message = "The category name is required.")
        @Size(max = 100, message = "The category name cannot exceed 100 characters.")
        String name,

        @Column("image_url")
        String imageUrl
) {
        public static Category of(String name)   {
                return new Category(null, name, "");
        }
}
