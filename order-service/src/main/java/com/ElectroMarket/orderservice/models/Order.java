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

        @Column("username")
        String username,
        @Column("total")
        Double total,

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
        public static Order of(String username, Double price, OrderStatus status)     {
                return new Order(null, username, price, status, null, null, 0);
        }
}
