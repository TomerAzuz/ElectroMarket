package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
public class ConfirmationSentEventJsonTests {
    @Autowired
    private JacksonTester<ConfirmationSentEvent> json;

    @Test
    void testSerialize() throws Exception   {
        var event = new ConfirmationSentEvent("success", 123L);
        var jsonContent = json.write(event);

        assertThat(jsonContent).extractingJsonPathStringValue("@.status")
                .isEqualTo(event.status());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.orderId")
                .isEqualTo(event.orderId().intValue());

    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "status": "success",
            "orderId": 123
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ConfirmationSentEvent("success", 123L));
    }
}
