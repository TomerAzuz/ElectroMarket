package unit;

import com.ElectroMarket.orderservice.dto.OrderRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRequestValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds()   {
        var orderRequest = new OrderRequest(1234L, 1);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIdNotDefinedThenValidationFails()  {
        var orderRequest = new OrderRequest(null, 1);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenQuantityIsNotDefinedThenValidationFails()  {
        var orderRequest = new OrderRequest(1234L, null);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The product quantity is required.");
    }

    @Test
    void whenQuantityIsLowerThanMinThenValidationFails()    {
        var orderRequest = new OrderRequest(12L, 0);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You must order at least 1 item.");
    }

    @Test
    void whenQuantityIsGreaterThanMaxThenValidationFails()  {
        var orderRequest = new OrderRequest(12L, 100);
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You cannot order more than 10 items.");
    }
}
