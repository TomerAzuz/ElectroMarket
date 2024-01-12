package com.ElectroMarket.mailservice;

import com.ElectroMarket.mailservice.events.ContactFormRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactFormValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThanValidationSucceeds()   {
        var contactForm = new ContactFormRequest("Tomer", "email@example.com", "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenNameIsBlankThenValidationFails()  {
        var contactForm = new ContactFormRequest("", "email@example.com", "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name is required.");
    }

    @Test
    void whenNameIsNotDefinedThenValidationFails()  {
        var contactForm = new ContactFormRequest(null, "email@example.com", "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Name is required.");
    }

    @Test
    void whenEmailIsInvalidThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "invalid", "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Invalid email address.");
    }

    @Test
    void whenEmailIsBlankThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "", "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Email is required.");
    }

    @Test
    void whenEmailIsNotDefinedThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", null, "subject", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Email is required.");
    }

    @Test
    void whenSubjectIsBlankThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "email@example.com", "", "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Subject is required.");
    }

    @Test
    void whenSubjectIsNotDefinedThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "email@example.com", null, "message");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Subject is required.");
    }

    @Test
    void whenMessageIsBlankThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "email@example.com", "subject", "");
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Message is required.");
    }

    @Test
    void whenMessageIsNotDefinedThenValidationFails()  {
        var contactForm = new ContactFormRequest("Tomer", "email@example.com", "subject", null);
        Set<ConstraintViolation<ContactFormRequest>> violations = validator.validate(contactForm);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Message is required.");
    }
}
