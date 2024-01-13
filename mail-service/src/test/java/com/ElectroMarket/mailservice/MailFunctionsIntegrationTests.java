package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.dto.EmailDetails;
import com.ElectroMarket.mailservice.events.ConfirmationMailRequest;
import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import com.ElectroMarket.mailservice.events.ContactFormRequest;
import com.ElectroMarket.mailservice.functions.MailFunctions;
import com.ElectroMarket.mailservice.service.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MailFunctionsIntegrationTests {


    @Mock
    private JavaMailSender mailSender;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private EmailServiceImpl emailService;


    @Test
    void sendMailSuccess() {
        EmailDetails emailDetails = new EmailDetails("recipient@example.com", "Subject", "Message", null);

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        StepVerifier.create(emailService.sendMail(emailDetails, 1L))
                .expectNextMatches(result -> result.status().equals("success") && result.orderId() == 1L)
                .verifyComplete();

        verify(streamBridge, times(1)).send(eq("confirmationSent-out-0"), any(ConfirmationSentEvent.class));
    }

    @Test
    void sendMailWithAttachmentSuccess() {
        EmailDetails emailDetails = new EmailDetails("recipient@example.com", "Subject", "Message", "file.txt");

        doNothing().when(mailSender).send(any(MimeMessage.class));

        StepVerifier.create(emailService.sendMailWithAttachment(emailDetails, 1L))
                .expectNextMatches(result -> result.status().equals("success") && result.orderId() == 1L)
                .verifyComplete();

        verify(streamBridge, times(1)).send(eq("confirmationSent-out-0"), any(ConfirmationSentEvent.class));
    }

    @Test
    public void testContact() {
        EmailServiceImpl emailService = mock(EmailServiceImpl.class);
        MailFunctions mailFunctions = new MailFunctions();
        ContactFormRequest contactFormRequest = new ContactFormRequest("John Doe", "john.doe@example.com", "Subject", "Message");
        Mono<@Valid ContactFormRequest> mono = Mono.just(contactFormRequest);

        when(emailService.sendMail(Mockito.any(), Mockito.any())).thenReturn(Flux.just(new ConfirmationSentEvent("success", null)));
        Mono<ConfirmationSentEvent> result = mailFunctions.contact(emailService).apply(mono);

        assertEquals("success", Objects.requireNonNull(result.block()).status());
        verify(emailService, times(1)).sendMail(Mockito.any(), Mockito.any());
    }

    @Test
    public void testConfirmationRequest() {
        EmailServiceImpl emailService = mock(EmailServiceImpl.class);
        MailFunctions mailFunctions = new MailFunctions();

        ConfirmationMailRequest confirmationMailRequest = new ConfirmationMailRequest(123L, "user@example.com");
        Flux<ConfirmationMailRequest> flux = Flux.just(confirmationMailRequest);

        when(emailService.consumeConfirmationRequestEvent(flux)).thenReturn(Flux.just(new ConfirmationSentEvent("success", 123L)));

        mailFunctions.confirmationRequest(emailService).accept(flux);

        verify(emailService, times(1)).consumeConfirmationRequestEvent(flux);
    }

    @Test
    void sendMailFailure() {
        EmailDetails failingEmailDetails = new EmailDetails("recipient@example.com", "Subject", "Message", null);
        doThrow(new RuntimeException("Mail sending failed")).when(mailSender).send(any(SimpleMailMessage.class));

        StepVerifier.create(emailService.sendMail(failingEmailDetails, 2L))
                .expectNextMatches(result -> result.status().equals("error") && result.orderId() == null)
                .verifyComplete();

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(streamBridge, times(0)).send(eq("confirmationSent-out-0"), any(ConfirmationSentEvent.class));
    }

    @Test
    void contactFailure() {
        EmailServiceImpl emailService = mock(EmailServiceImpl.class);
        MailFunctions mailFunctions = new MailFunctions();
        ContactFormRequest contactFormRequest = new ContactFormRequest("John Doe", "john.doe@example.com", "Subject", "Message");
        Mono<@Valid ContactFormRequest> mono = Mono.just(contactFormRequest);

        when(emailService.sendMail(Mockito.any(), Mockito.any())).thenReturn(Flux.error(new RuntimeException("Email sending failed")));
        Mono<ConfirmationSentEvent> result = mailFunctions.contact(emailService).apply(mono);

        assertEquals("error", Objects.requireNonNull(result.block()).status());
        verify(emailService, times(1)).sendMail(Mockito.any(), Mockito.any());
    }

    @Test
    void confirmationRequestFailure() {
        EmailServiceImpl emailService = mock(EmailServiceImpl.class);
        MailFunctions mailFunctions = new MailFunctions();

        ConfirmationMailRequest confirmationMailRequest = new ConfirmationMailRequest(123L, "user@example.com");
        Flux<ConfirmationMailRequest> flux = Flux.just(confirmationMailRequest);

        when(emailService.consumeConfirmationRequestEvent(flux)).thenReturn(Flux.error(new RuntimeException("Confirmation processing failed")));

        mailFunctions.confirmationRequest(emailService).accept(flux);

        verify(emailService, times(1)).consumeConfirmationRequestEvent(flux);
    }
}
