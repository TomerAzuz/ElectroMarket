package com.ElectroMarket.catalogservice.models;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.*;

import java.time.Instant;

public record Product(
        @Id
        Long id,

        @NotBlank(message = "The product name is required.")
        @Size(max = 255, message = "The product name cannot exceed 255 characters.")
        String name,

        @Size(max = 1000, message = "The product description cannot exceed 1000 characters.")
        String description,

        @NotNull(message = "The product price is required.")
        @Positive(message = "The product price must be greater than zero.")
        Double price,

        @NotNull(message = "The product category id is required.")
        Long categoryId,

        @Min(value = 0, message = "Stock level cannot be negative.")
        Integer stock,

        @Pattern(
                regexp = "^(https?|ftp)://[A-Za-z0-9+&@#/%?=~_|!:,.;]*[-A-Za-z0-9+&@#/%=~_|]",
                message = "Invalid image url."
        )
        String imageUrl,

        @CreatedDate
        Instant createdDate,

        @LastModifiedDate
        Instant lastModifiedDate,

        @Version
        int version
) {
    public static Product of(
            String name, String description,
            Double price, Long categoryId,
            Integer stock, String imageUrl
    )   {
        return new Product(null, name, description, price, categoryId, stock, imageUrl, null, null, 0);
    }
}


