package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.models.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderItemJsonTests {

    @Autowired
    private JacksonTester<OrderItem> json;

    @Test
    void testSerialize() throws Exception   {
        var now = Instant.now();
        var item = new OrderItem(1L, 1L, 1L, 2, now, now, "Tomer", "Tomer", 0);
        var jsonContent = json.write(item);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id")
                .isEqualTo(item.id().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.orderId")
                .isEqualTo(item.orderId().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.productId")
                .isEqualTo(item.productId().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.quantity")
                .isEqualTo(item.quantity());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdBy")
                .isEqualTo(item.createdBy());
    }

    @Test
    void testDeserialize() throws Exception {
        var instant = Instant.parse("2023-09-10T13:48:51.199355Z");
        var content = """
                {
                    "id": 1,
                    "orderId": 1,
                    "productId": 1,
                    "quantity": 1,
                    "createdDate": "2023-09-10T13:48:51.199355Z",
                    "createdBy": "tomer"
                }
                """;
        var expectedItem = new OrderItem(1L, 1L, 1L, 1, instant, null, "tomer", null, 0);
        assertThat(this.json.parse(content))
                .usingRecursiveComparison().isEqualTo(expectedItem);
    }
}
