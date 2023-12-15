package com.ElectroMarket.catalogservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.*;

import java.time.Instant;

public record Product(
        @Id
        Long id,

        @NotBlank(message = "The product name is required.")
        @Size(max = 255, message = "The product name cannot exceed 255 characters.")
        String name,

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

        @NotBlank(message = "The product brand is required.")
        @Size(max = 255, message = "The product brand cannot exceed 255 characters.")
        String brand,

        @JsonIgnore
        @CreatedDate
        Instant createdDate,

        @JsonIgnore
        @LastModifiedDate
        Instant lastModifiedDate,

        @JsonIgnore
        @CreatedBy
        String createdBy,

        @JsonIgnore
        @LastModifiedBy
        String lastModifiedBy,

        @JsonIgnore
        @Version
        int version
) {
    public static Product of(
            String name, Double price,
            Long categoryId, Integer stock,
            String imageUrl, String brand

    )   {
        return new Product(null, name, price, categoryId, stock,
                            imageUrl, brand, null, null, null, null, 0);
    }
}
