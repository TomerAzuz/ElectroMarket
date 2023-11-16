package com.ElectroMarket.orderservice.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("order_items")
public record OrderItem(
    @Id
    Long id,
    @Column("order_id")
    Long orderId,
    @Column("product_id")
    @NotNull(message = "The product id must be defined.")
    Long productId,
    @Column("quantity")
    @NotNull(message = "The item quantity must be defined.")
    @Min(value = 1, message = "You must order at least 1 item.")
    @Max(value = 3, message = "You cannot order more than 3 items.")
    Integer quantity,

    @CreatedDate
    @Column("created_date")
    Instant createdDate,

    @LastModifiedDate
    @Column("last_modified_date")
    Instant lastModifiedDate,

    @CreatedBy
    @Column("created_by")
    String createdBy,

    @LastModifiedBy
    @Column("last_modified_by")
    String lastModifiedBy,

    @Version
    int version
) {

    public static OrderItem of(Long orderID, Long productId, Integer quantity)  {
        return new OrderItem(null,
                orderID,
                productId,
                quantity,
                null,
                null,
                null,
                null,
                0);
    }
}
