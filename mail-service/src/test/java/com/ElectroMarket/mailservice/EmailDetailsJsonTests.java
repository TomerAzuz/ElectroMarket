package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.dto.EmailDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class EmailDetailsJsonTests {

    @Autowired
    private JacksonTester<EmailDetails> json;

    @Test
    void testSerialize() throws Exception   {
        var email = new EmailDetails("recipient", "subject", "message", "attachment");
        var jsonContent = json.write(email);

        assertThat(jsonContent).extractingJsonPathStringValue("@.recipient")
                .isEqualTo(email.recipient());
        assertThat(jsonContent).extractingJsonPathStringValue("@.subject")
                .isEqualTo(email.subject());
        assertThat(jsonContent).extractingJsonPathStringValue("@.message")
                .isEqualTo(email.message());
        assertThat(jsonContent).extractingJsonPathStringValue("@.attachment")
                .isEqualTo(email.attachment());

    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "recipient": "recipient",
            "subject": "subject",
            "message": "message",
            "attachment": "attachment"
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new EmailDetails("recipient", "subject", "message", "attachment"));
    }
}
