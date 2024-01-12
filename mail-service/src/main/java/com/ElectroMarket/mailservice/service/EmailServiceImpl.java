package com.ElectroMarket.mailservice.service;

import com.ElectroMarket.mailservice.constants.EmailConstants;
import com.ElectroMarket.mailservice.dto.EmailDetails;
import com.ElectroMarket.mailservice.events.ConfirmationMailRequest;
import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;

import static com.ElectroMarket.mailservice.constants.EmailConstants.ELECTROMARKET_EMAIL;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final StreamBridge streamBridge;
    private final JavaMailSender mailSender;
    private final String sender;

    public EmailServiceImpl(JavaMailSender mailSender,
                            @Value("${spring.mail.username}") String sender,
                            StreamBridge streamBridge) {
        this.mailSender = mailSender;
        this.sender = sender;
        this.streamBridge = streamBridge;
    }

    public Flux<ConfirmationSentEvent> sendMail(EmailDetails email, Long orderId) {
        return Flux.defer(() -> Mono.fromCallable(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();

                message.setFrom(sender);
                message.setTo(email.recipient());
                message.setText(email.message());
                message.setSubject(email.subject());

                mailSender.send(message);

                ConfirmationSentEvent response = new ConfirmationSentEvent("success", orderId);
                publishConfirmationSentEvent(response);
                return response;
            } catch (Exception e) {
                log.error("Error sending email", e);
                return new ConfirmationSentEvent("failure", null);
            }
        })).doOnNext(result -> log.info("Mail sent: {}", result.status()));
    }

    public Flux<ConfirmationSentEvent> sendMailWithAttachment(EmailDetails email, Long orderId) {
        return Flux.defer(() -> Mono.fromCallable(() -> {
            try {
                mailSender.send(mimeMessage -> {
                    try {
                        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                        mimeMessageHelper.setFrom(sender);
                        mimeMessageHelper.setTo(email.recipient());
                        mimeMessageHelper.setText(email.message());
                        mimeMessageHelper.setSubject(email.subject());

                        FileSystemResource fileResource = new FileSystemResource(new File(email.attachment()));

                        if (fileResource.exists()) {
                            mimeMessageHelper.addAttachment(fileResource.getFilename(), fileResource);
                        } else {
                            throw new IllegalArgumentException("Attachment file does not exist: " + email.attachment());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return new ConfirmationSentEvent("success", orderId);
            } catch (Exception e) {
                log.error("Error sending email with attachment", e);
                return new ConfirmationSentEvent("error", null);
            }
        })).doOnNext(result -> log.info("Mail with attachment sent {}", result.status()));
    }

    public Flux<ConfirmationSentEvent> consumeConfirmationRequestEvent(Flux<ConfirmationMailRequest> event)  {
        return event.flatMap(request -> {
            EmailDetails email = buildConfirmationEmail(request);
            return sendMail(email, request.orderId())
                    .doOnNext(result -> log.info("Order confirmation email sent: {}", result));
        });
    }

    EmailDetails buildConfirmationEmail(ConfirmationMailRequest request)   {
        return new EmailDetails(
                request.userEmail(),
                EmailConstants.CONFIRMATION_EMAIL_SUBJECT,
                EmailConstants.formatConfirmationEmailBody(
                        request.orderId(),
                        ELECTROMARKET_EMAIL,
                        "ElectroMarket"),
                null);
    }

    public void publishConfirmationSentEvent(ConfirmationSentEvent response) {
        log.info("Sending confirmation sent event for order ID: {}, status: {}", response.orderId(), response.status());
        var result = streamBridge.send("confirmationSent-out-0", response);
        log.info("Result of sending data for order with ID {}: {}", response.orderId(), result);
    }
}
