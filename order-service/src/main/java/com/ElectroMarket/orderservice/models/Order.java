package com.ElectroMarket.orderservice.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("orders")
public record Order (
        @Id
        Long id,

        @Column("product_id")
        Long productId,

        @Column("product_name")
        String productName,

        @Column("product_price")
        Double productPrice,

        @Column("quantity")
        Integer quantity,

        @Column("status")
        OrderStatus status,

        @CreatedDate
        @Column("created_date")
        Instant createdDate,

        @LastModifiedDate
        @Column("last_modified_date")
        Instant lastModifiedDate,

        @Version
        int version

) {
  public static Order of(
  Long productId, String productName,
  Double productPrice, Integer quantity, OrderStatus status
  )     {
          return new Order(null, productId, productName, productPrice, quantity, status, null, null, 0);
  }
}
