package az.code.tourapi.models.dtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class LoginDTOTest {

    private static final LoginDTO baseLoginDTO = new LoginDTO();
    private static Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseLoginDTO.setEmail("parvinyy@code.edu.az");
        baseLoginDTO.setPassword("12345678");
    }

    @Test
    @DisplayName("LoginDTO - Invalid email")
    public void LoginDTO_Invalid_Email() {
        baseLoginDTO.setEmail("test");
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertEquals("Email must be a well-formed email address", violations.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("LoginDTO - Valid email")
    public void LoginDTO_Valid_Email() {
        baseLoginDTO.setEmail("test@test.com");
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("LoginDTO - Null email")
    public void LoginDTO_Null_Email() {
        baseLoginDTO.setEmail(null);
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertEquals("Email must not be null or empty", violations.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("LoginDTO - Invalid password")
    public void LoginDTO_Invalid_Password() {
        baseLoginDTO.setPassword("123");
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertEquals("Password must be at 6-15 characters long", violations.stream().findFirst().get().getMessage());
    }

    @Test
    @DisplayName("LoginDTO - Valid password")
    public void LoginDTO_Valid_Password() {
        baseLoginDTO.setPassword("123456789");
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("LoginDTO - Null password")
    public void LoginDTO_Null_Password() {
        baseLoginDTO.setPassword(null);
        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(baseLoginDTO);
        assertEquals("Password must not be null or empty", violations.stream().findFirst().get().getMessage());
    }
}