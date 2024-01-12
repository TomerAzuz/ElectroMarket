package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.events.ConfirmationMailRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ConfirmationMailRequestJsonTests {

    @Autowired
    private JacksonTester<ConfirmationMailRequest> json;

    @Test
    void testSerialize() throws Exception   {
        var request = new ConfirmationMailRequest(123L, "email@example.com");
        var jsonContent = json.write(request);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.orderId")
                .isEqualTo(request.orderId().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.userEmail")
                .isEqualTo(request.userEmail());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "orderId": 123,
            "userEmail": "email@example.com"
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ConfirmationMailRequest(123L, "email@example.com"));
    }
}
