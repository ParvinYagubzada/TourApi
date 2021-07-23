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
class RegisterDTOTest {

    private static RegisterDTO baseDTO;
    private static Validator validator;
    private static Set<ConstraintViolation<RegisterDTO>> violations;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseDTO = new RegisterDTO();
    }

    @Test
    @DisplayName("RegisterDTO - Valid")
    public void RegisterDTO_Valid() {
        baseDTO.setAgencyName("This is a agent name");
        baseDTO.setVoen("1234567890");
        baseDTO.setUsername("test");
        baseDTO.setName("test");
        baseDTO.setSurname("test");
        baseDTO.setEmail("test@test.com");
        baseDTO.setPassword("123456789");
        violations = validator.validate(baseDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("RegisterDTO - Violated")
    public void RegisterDTO_Violated() {
        baseDTO.setAgencyName("n");
        baseDTO.setVoen("12345678901");
        baseDTO.setUsername("testtesttesttesttesttest");
        baseDTO.setName("t");
        baseDTO.setSurname("t");
        baseDTO.setEmail("test");
        baseDTO.setPassword("123");
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(7)
                .map(ConstraintViolation::getMessage)
                .contains("Agency name must be at 2-30 characters long", "Username must be at 4-20 characters long",
                        "VOEN should contain only 10 digits", "Email must be a well-formed email address",
                        "Surname must be at 2-30 characters long", "Password must be at 6-15 characters long",
                        "Name must be at 2-30 characters long");
        baseDTO.setAgencyName(null);
        baseDTO.setVoen("");
        baseDTO.setUsername(null);
        baseDTO.setName("       ");
        baseDTO.setSurname(null);
        baseDTO.setEmail(null);
        baseDTO.setPassword(null);
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(7)
                .map(ConstraintViolation::getMessage)
                .contains("Name must not be null or empty", "Username must not be null or empty",
                        "Agency name must not be null or empty", "Password must not be null or empty",
                        "VOEN should contain only 10 digits", "Email must not be null or empty",
                        "Surname must not be null or empty");
    }
}