package com.ElectroMarket.mailservice.service;

import com.ElectroMarket.mailservice.dto.EmailDetails;
import com.ElectroMarket.mailservice.events.ConfirmationSentEvent;
import reactor.core.publisher.Flux;

public interface EmailService {
    Flux<ConfirmationSentEvent> sendMail(EmailDetails email, Long orderId);
    Flux<ConfirmationSentEvent>sendMailWithAttachment(EmailDetails email, Long orderId);
}
