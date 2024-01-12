package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.models.Order;
import com.ElectroMarket.orderservice.models.OrderItem;
import com.ElectroMarket.orderservice.models.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderJsonTests {

    @Autowired
    private JacksonTester<Order> json;

    @Test
    void testSerialize() throws Exception {
        var order = new Order(25L, "Tomer", BigDecimal.valueOf(1250), OrderStatus.PAYMENT_PENDING, Instant.now(), Instant.now(), "tomer123", 0);
        var jsonContent = json.write(order);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(order.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdBy")
                .isEqualTo(order.createdBy());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.total")
                .isEqualTo(order.total().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(order.status().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(order.createdDate().toString());
    }

    @Test
    void testDeserialize() throws Exception {
        var instant = Instant.parse("2023-09-10T13:48:51.199355Z");
        var content = """
                {
                    "id": 1,
                    "createdBy": "Tomer",
                    "total": 100,
                    "status": "CONFIRMATION_SENT",
                    "createdDate": "2023-09-10T13:48:51.199355Z",
                    "lastModifiedBy": "Tomer",
                    "version": 0
                }
                """;
        var expectedOrder = new Order(1L, "Tomer", BigDecimal.valueOf(100), OrderStatus.CONFIRMATION_SENT, instant, null, null, 0);
        assertThat(this.json.parse(content))
                .usingRecursiveComparison().isEqualTo(expectedOrder);
    }
}
