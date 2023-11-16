package com.ElectroMarket.orderservice.models;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("orders")
public record Order (
        @Id
        Long id,

        @CreatedBy
        @Column("created_by")
        String createdBy,

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

        @LastModifiedBy
        @Column("last_modified_by")
        String lastModifiedBy,

        @Version
        int version

) {
        public static Order of(Double price, OrderStatus status)     {
                return new Order(null, null, price, status, null, null, null,0);
        }
}
