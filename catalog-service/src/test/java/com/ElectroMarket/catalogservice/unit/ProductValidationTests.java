package com.ElectroMarket.catalogservice.unit;


import com.ElectroMarket.catalogservice.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validFields()  {
        var product = Product.of("product", BigDecimal.valueOf(10.5), 1L, 10, "https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).isEmpty();
    }

    @Test
    void tooLongProductName()   {
        String longName = "a".repeat(256);
        var product = Product.of(longName, BigDecimal.valueOf(5.0), 1L, 10, "https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product name cannot exceed 255 characters.");
    }

    @Test
    void undefinedName()   {
        var product = Product.of( "", BigDecimal.valueOf(5.0), 1L, 10,"https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product name is required.");
    }

    @Test
    void undefinedCategory()   {
        var product = Product.of("product", BigDecimal.valueOf(8.5), null, 10, "https://example.com/image.jpg","brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product category id is required.");
    }

    @Test
    void undefinedPrice()   {
        var product = Product.of("product", null, 1L, 10, "https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product price is required.");
    }

    @Test
    void negativePrice()    {
        var product = Product.of("product", BigDecimal.valueOf(-5.0), 1L, 10, "https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product price must be greater than zero.");
    }

    @Test
    void negativeStock() {
        var product = Product.of("product", BigDecimal.valueOf(9.99), 2L, -2, "https://example.com/image.jpg", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Stock level cannot be negative.");
    }

    @Test
    void testInvalidImageUrl()  {
        var product = Product.of("product", BigDecimal.valueOf(9.99), 1L, 10, "invalid-url", "brand");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid image url.");
    }

    @Test
    void testLongBrand()  {
        String longBrand = "a".repeat(256);
        var product = Product.of("product", BigDecimal.valueOf(9.99), 1L, 10, "https://example.com/image.jpg", longBrand);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product brand cannot exceed 255 characters.");
    }
}
