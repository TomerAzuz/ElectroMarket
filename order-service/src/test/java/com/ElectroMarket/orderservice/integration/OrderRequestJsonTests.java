package com.ElectroMarket.orderservice.integration;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class OrderRequestJsonTests {

    @Autowired
    private JacksonTester<OrderRequest> json;

    @Test
    void testDeserialize() throws Exception {
        var content = """
                {
                    "product_id": 1234,
                    "quantity": 2
                }
                """;
        assertThat(this.json.parse(content))
                .usingRecursiveComparison().isEqualTo(new OrderRequest(1234L, 2));
    }
}
