package com.ElectroMarket.catalogservice.unit;

import com.ElectroMarket.catalogservice.models.Category;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void validFields()  {
        var category = Category.of("Laptops", 2L);
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations).isEmpty();
    }

    @Test
    void tooLongCategoryName()   {
        String longName = "a".repeat(101);
        var category = Category.of(longName, null);
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The category name cannot exceed 100 characters.");
    }

    @Test
    void undefinedName()   {
        var category = Category.of( "", null);
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The category name is required.");
    }
}
