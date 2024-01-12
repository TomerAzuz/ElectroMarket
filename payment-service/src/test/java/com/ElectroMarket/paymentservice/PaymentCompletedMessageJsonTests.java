package com.ElectroMarket.paymentservice;

import com.ElectroMarket.paymentservice.dto.PaymentCompletedMessage;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PaymentCompletedMessageJsonTests {

    @Autowired
    private JacksonTester<PaymentCompletedMessage> json;

    @Test
    void testSerialize() throws Exception   {
        var response = new PaymentCompletedMessage("COMPLETED", 123L);
        var jsonContent = json.write(response);
        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(response.status());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.orderId")
                .isEqualTo(response.orderId().intValue());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "status": "COMPLETED",
            "orderId": 123
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new PaymentCompletedMessage("COMPLETED", 123L));
    }
}
