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
class OfferDTOTest {

    private static OfferDTO baseDTO;
    private static Validator validator;
    private static Set<ConstraintViolation<OfferDTO>> violations;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        baseDTO = new OfferDTO();
    }

    @Test
    @DisplayName("OfferDTO - Valid")
    public void LoginDTO_Valid() {
        baseDTO.setDescription("This is a test Description");
        baseDTO.setTravelDates("12.01.2021-12.12.2021");
        baseDTO.setPrice(1234);
        baseDTO.setNotes("This is a test Note");
        violations = validator.validate(baseDTO);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("ResetPasswordDTO - Violated")
    public void LoginDTO_Violated() {
        baseDTO.setDescription("test");
        baseDTO.setTravelDates("33.14.12");
        baseDTO.setPrice(1111111111);
        baseDTO.setNotes("null");
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(4)
                .map(ConstraintViolation::getMessage)
                .contains("Description length must be min 10 and max 1000 characters long",
                        "Travel dates must be like XX.XX.XXXX-XX.XX.XXXX format.",
                        "Price can contain max 9 digits",
                        "Notes length must be min 10 and max 1000 characters long");
        baseDTO.setDescription(null);
        baseDTO.setTravelDates(null);
        baseDTO.setPrice(null);
        baseDTO.setNotes(null);
        violations = validator.validate(baseDTO);
        assertThat(violations)
                .hasSize(4)
                .map(ConstraintViolation::getMessage)
                .contains("Description must not be null", "Travel dates must not be null",
                        "Price must not be null", "Notes must not be null");
    }
}