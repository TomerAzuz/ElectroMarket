package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderJsonTests {

    @Autowired
    private JacksonTester<Order> json;

    @Test
    void testSerialize() throws Exception {
        var order = new Order(25L, 1L, "product", 9.99, 4, OrderStatus.ACCEPTED, Instant.now(), Instant.now(), 45);
        var jsonContent = json.write(order);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(order.id().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.productId")
                .isEqualTo(order.productId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.productName")
                .isEqualTo(order.productName());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.productPrice")
                .isEqualTo(order.productPrice());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.quantity")
                .isEqualTo(order.quantity());
        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(order.status().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(order.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(order.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version")
                .isEqualTo(order.version());
    }

}
