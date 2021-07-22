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
class UpdatePasswordDTOTest {

    private static UpdatePasswordDTO baseDTO;
    private static Validator validator;
    private static Set<ConstraintViolation<UpdatePasswordDTO>> violations;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseDTO = new UpdatePasswordDTO();
        baseDTO.setOldPassword("123456789");
    }

    @Test
    @DisplayName("UpdatePasswordDTO - Valid")
    public void LoginDTO_Valid() {
        baseDTO.setNewPassword("123456789");
        violations = validator.validate(baseDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("UpdatePasswordDTO - Violated")
    public void LoginDTO_Violated() {
        baseDTO.setNewPassword("123");
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(1)
                .map(ConstraintViolation::getMessage)
                .contains("Password must be at 6-15 characters long");
        baseDTO.setNewPassword(null);
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(1)
                .map(ConstraintViolation::getMessage)
                .contains("Password must not be null");
    }
}