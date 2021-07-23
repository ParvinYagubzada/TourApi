package az.code.tourapi.models.dtos;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodName.class)
class LoginDTOTest {

    private static LoginDTO baseDTO;
    private static Validator validator;
    private static Set<ConstraintViolation<LoginDTO>> violations;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseDTO = new LoginDTO();
    }

    @Test
    @DisplayName("LoginDTO - Valid")
    public void LoginDTO_Valid() {
        baseDTO.setEmail("test@test.com");
        baseDTO.setPassword("123456789");
        violations = validator.validate(baseDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("LoginDTO - Violated")
    public void LoginDTO_Violated() {
        baseDTO.setEmail("test");
        baseDTO.setPassword("123");
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(2)
                .map(ConstraintViolation::getMessage)
                .contains("Email must be a well-formed email address", "Password must be at 6-15 characters long");
        baseDTO.setEmail(null);
        baseDTO.setPassword(null);
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(2)
                .map(ConstraintViolation::getMessage)
                .contains("Email must not be null", "Password must not be null or empty");
    }
}