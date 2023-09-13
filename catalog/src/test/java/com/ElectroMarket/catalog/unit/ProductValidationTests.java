package com.ElectroMarket.catalog.unit;


import com.ElectroMarket.catalog.models.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
        var product = Product.of("product", "Some product.", 10.5, 1L, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).isEmpty();
    }

    @Test
    void tooLongProductName()   {
        String longName = "a".repeat(256);
        var product = Product.of(longName, "description", 5.0, 1L, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product name cannot exceed 255 characters.");
    }

    @Test
    void tooLongDescription()   {
        String longDescription = "a".repeat(1001);
        var product = Product.of("product", longDescription, 5.0, 1L, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product description cannot exceed 1000 characters.");
    }

    @Test
    void undefinedName()   {
        var product = Product.of( "", "description", 5.0, 1L, 10,"https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product name is required.");
    }

    @Test
    void undefinedCategory()   {
        var product = Product.of("product", "description", 8.5, null, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product category id is required.");
    }

    @Test
    void undefinedPrice()   {
        var product = Product.of("product", "description", null, 1L, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product price is required.");
    }

    @Test
    void negativePrice()    {
        var product = Product.of("product", "description", -5.0, 1L, 10, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The product price must be greater than zero.");
    }

    @Test
    void negativeStock() {
        var product = Product.of("product", "description", 9.99, 2L, -2, "https://example.com/image.jpg");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Stock level cannot be negative.");
    }

    @Test
    void testInvalidImageUrl() throws Exception {
        var product = Product.of("product", "description", 9.99, 1L, 10, "invalid-url");
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Invalid image url.");
    }
}
