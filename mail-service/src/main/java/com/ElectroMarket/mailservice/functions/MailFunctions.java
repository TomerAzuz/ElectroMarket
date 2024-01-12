package com.ElectroMarket.mailservice.functions;

import com.ElectroMarket.mailservice.events.ConfirmationMailRequest;
import com.ElectroMarket.mailservice.events.ContactFormRequest;
import com.ElectroMarket.mailservice.dto.EmailDetails;
import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import com.ElectroMarket.mailservice.service.EmailServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.ElectroMarket.mailservice.constants.EmailConstants.ELECTROMARKET_EMAIL;
import static com.ElectroMarket.mailservice.constants.EmailConstants.formatContactFormMessage;

@Configuration
public class MailFunctions {
    private static final Logger log = LoggerFactory.getLogger(MailFunctions.class);

    @Bean
    public Function<Flux<EmailDetails>, Flux<ConfirmationSentEvent>> sendMail(EmailServiceImpl emailService) {
        return flux -> flux.flatMap(email -> emailService.sendMail(email, null));
    }

    @Bean
    public Function<Flux<EmailDetails>, Flux<ConfirmationSentEvent>> sendMailWithAttachment(EmailServiceImpl emailService) {
        return flux -> flux.flatMap(email -> emailService.sendMailWithAttachment(email, null));
    }

    @Bean
    public Function<Mono<@Valid ContactFormRequest>, Mono<ConfirmationSentEvent>> contact(EmailServiceImpl emailService) {
        return mono -> mono.flatMap(form -> {
            log.info("Received message via contact form");

            String subject = "New Message from " + form.name() + ": " + form.subject();
            String message = formatContactFormMessage(form.name(), form.email(), form.subject(), form.message());

            EmailDetails email = new EmailDetails(ELECTROMARKET_EMAIL, subject, message, null);

            return emailService.sendMail(email, null)
                    .next()
                    .doOnNext(result -> log.info("Contact form confirmation email sent: {}", result))
                    .doOnError(e -> log.error("Error processing contact form", e))
                    .onErrorReturn(new ConfirmationSentEvent("error", null));
        });
    }

    @Bean
    public Consumer<Flux<ConfirmationMailRequest>> confirmationRequest(EmailServiceImpl emailService) {
        return request -> emailService.consumeConfirmationRequestEvent(request)
                .doOnNext(event -> log.info("Payment status for order with id {}: {}", event.orderId(), event.status()))
                .subscribe();
    }
}
