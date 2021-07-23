package az.code.tourapi.models.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodName.class)
class ResetPasswordDTOTest {

    private static ResetPasswordDTO baseDTO;
    private static Validator validator;
    private static Set<ConstraintViolation<ResetPasswordDTO>> violations;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseDTO = new ResetPasswordDTO();
        baseDTO.setToken("test");
        baseDTO.setUsername("test");
    }

    @Test
    @DisplayName("ResetPasswordDTO - Valid")
    public void ResetPasswordDTO_Valid() {
        baseDTO.setPassword("123456789");
        violations = validator.validate(baseDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("ResetPasswordDTO - Violated")
    public void ResetPasswordDTO_Violated() {
        baseDTO.setPassword("123");
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(1)
                .map(ConstraintViolation::getMessage)
                .contains("Password must be at 6-15 characters long");
        baseDTO.setPassword(null);
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(1)
                .map(ConstraintViolation::getMessage)
                .contains("Password must not be null or empty");
    }
}