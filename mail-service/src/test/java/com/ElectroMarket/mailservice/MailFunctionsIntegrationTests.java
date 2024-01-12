package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.dto.EmailDetails;
import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import com.ElectroMarket.mailservice.events.ContactFormRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Function;

import static com.ElectroMarket.mailservice.constants.EmailConstants.ELECTROMARKET_EMAIL;

@FunctionalSpringBootTest
public class MailFunctionsIntegrationTests {

    @Autowired
    private FunctionCatalog catalog;

    @TempDir
    Path tempDir;


    @Test
    void sendMail() {
        Function<Flux<EmailDetails>, Flux<ConfirmationSentEvent>> sendMail = catalog.lookup(Function.class, "sendMail");
        var email = Flux.just(new EmailDetails(ELECTROMARKET_EMAIL, "subject", "message", null));

        StepVerifier.create(sendMail.apply(email))
                .expectNextMatches(result -> "success".equals(result.status()))
                .verifyComplete();
    }

    @Test
    void sendMailWithAttachment() {
        Function<Flux<EmailDetails>, Flux<ConfirmationSentEvent>> sendMailWithAttachment = catalog.lookup(Function.class, "sendMailWithAttachment");

        File attachment = new File(tempDir.toFile(), "attachment.txt");
        String fileContent = "This is the content of the attachment file.";

        try (FileOutputStream fos = new FileOutputStream(attachment)) {
            fos.write(fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var email = Flux.just(new EmailDetails(ELECTROMARKET_EMAIL, "subject", "message", attachment.getAbsolutePath()));

        StepVerifier.create(sendMailWithAttachment.apply(email))
                .expectNextMatches(event -> "success".equals(event.status()))
                .verifyComplete();
    }

    @Test
    void contact()   {
        Function<Mono<ContactFormRequest>, Mono<ConfirmationSentEvent>> contact = catalog.lookup(Function.class, "contact");
        var contactForm = Mono.just(new ContactFormRequest("Tomer", "email@example.com", "subject", "message"));
        StepVerifier.create(contact.apply(contactForm))
                .expectNextMatches(result -> "success".equals(result.status()))
                .verifyComplete();
    }
}
