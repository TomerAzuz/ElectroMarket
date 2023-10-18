package unit;

import com.ElectroMarket.orderservice.models.OrderItem;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderItemValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds()   {
        var item = OrderItem.of(null, 1L, 1);
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(item);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenProductIdNotDefinedThenValidationFails()  {
        var invalidItem = OrderItem.of(null, null, 1);
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(invalidItem);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The product id must be defined.");
    }

    @Test
    void whenQuantityIsNotDefinedThenValidationFails()  {
        var invalidItem = OrderItem.of(null, 1L, null);
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(invalidItem);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The item quantity must be defined.");
    }

    @Test
    void whenQuantityIsLowerThanMinThenValidationFails()    {
        var invalidItem = OrderItem.of(null, 1L, 0);
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(invalidItem);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You must order at least 1 item.");
    }

    @Test
    void whenQuantityIsGreaterThanMaxThenValidationFails()  {
        var invalidItem = OrderItem.of(null, 1L, 10);
        Set<ConstraintViolation<OrderItem>> violations = validator.validate(invalidItem);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You cannot order more than 3 items.");
    }
}
