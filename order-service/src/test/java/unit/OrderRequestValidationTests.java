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
    }

    @Test
    void whenIdNotDefinedThenValidationFails()  {
    }

    @Test
    void whenQuantityIsNotDefinedThenValidationFails()  {
    }

    @Test
    void whenQuantityIsLowerThanMinThenValidationFails()    {
    }

    @Test
    void whenQuantityIsGreaterThanMaxThenValidationFails()  {
    }
}
