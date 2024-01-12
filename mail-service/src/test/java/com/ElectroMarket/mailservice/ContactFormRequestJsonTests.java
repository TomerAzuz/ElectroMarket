package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.events.ContactFormRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ContactFormRequestJsonTests {

    @Autowired
    private JacksonTester<ContactFormRequest> json;

    @Test
    void testSerialize() throws Exception   {
        var request = new ContactFormRequest("Tomer", "email@example.com", "Greeting", "Hi there!");
        var jsonContent = json.write(request);

        assertThat(jsonContent).extractingJsonPathStringValue("@.name")
                .isEqualTo(request.name());
        assertThat(jsonContent).extractingJsonPathStringValue("@.email")
                .isEqualTo(request.email());
        assertThat(jsonContent).extractingJsonPathStringValue("@.subject")
                .isEqualTo(request.subject());
        assertThat(jsonContent).extractingJsonPathStringValue("@.message")
                .isEqualTo(request.message());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """
        {
            "name": "Tomer",
            "email": "email@example.com",
            "subject": "Greeting",
            "message": "Hi there!"
        }
        """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new ContactFormRequest("Tomer", "email@example.com", "Greeting", "Hi there!"));
    }
}
