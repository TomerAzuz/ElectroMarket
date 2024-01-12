package com.ElectroMarket.paymentservice;

import com.ElectroMarket.paymentservice.dto.PaymentRequestMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

@JsonTest
public class PaymentRequestMessageJsonTests {

    @Autowired
    private JacksonTester<PaymentRequestMessage> json;

    @Test
    void testSerialize() throws Exception {
        var request = new PaymentRequestMessage(123L, BigDecimal.ONE);
        var jsonContent = json.write(request);
        assertThat(jsonContent).extractingJsonPathNumberValue("@.orderId")
                .isEqualTo(request.orderId().intValue());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.total")
                .isEqualTo(request.total().intValue());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "orderId": 123,
            "total": 10.00
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new PaymentRequestMessage(123L, new BigDecimal("10.00")));
    }
}
