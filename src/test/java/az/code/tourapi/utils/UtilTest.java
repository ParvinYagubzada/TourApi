package az.code.tourapi.utils;

import az.code.tourapi.exceptions.EmailNotVerified;
import az.code.tourapi.exceptions.InvalidTokenFormat;
import az.code.tourapi.models.UserData;
import az.code.tourapi.models.dtos.OfferDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static az.code.tourapi.TourApiApplicationTests.*;
import static az.code.tourapi.utils.Mappers.timeFormatter;
import static az.code.tourapi.utils.Util.convertToken;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("SpellCheckingInspection")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class UtilTest {

    @Test
    @DisplayName("Util - checkTime - Valid")
    void checkTime() {
        LocalTime now = LocalTime.parse("01:00:00", timeFormatter);
        String start = "08:00:00";
        String end = "19:00:00";
        assertTrue(Util.checkTime(start, end, now));
        now = LocalTime.parse("09:00:00", timeFormatter);
        assertFalse(Util.checkTime(start, end, now));
    }

    @Test
    @DisplayName("Util - createImage - Valid")
    void createImage() {
        OfferDTO dto = OfferDTO.builder()
                .description("salary").travelDates("time")
                .price(386).notes("sock")
                .build();
        File image = Util.createImage(dto);
        assertTrue(image.exists());
        assertTrue(image.delete());
    }

    @Test
    @DisplayName("Util - convertToken - Valid")
    void convertTokenValid() {
        UserData expected = new UserData();
        expected.setAgencyName(AGENCY_NAME);
        expected.setUsername("pervinuser");
        expected.setFullName("Pervin Yaqubzade");
        expected.setEmail("parvinyy@code.edu.az");
        expected.setRegistrationTime(LocalDateTime.ofEpochSecond(1626807375037L, 0, ZoneOffset.ofHours(4)));
        assertEquals(expected, convertToken(VALID_TOKEN));
    }

    @Test
    @DisplayName("Util - convertToken - Invalid Format")
    void convertTokenInvalidFormat() {
        assertThrows(InvalidTokenFormat.class, () -> convertToken(TEST_STRING));
    }

    @Test
    @DisplayName("Util - convertToken - Invalid Value")
    void convertTokenInvalidValue() {
        assertThrows(InvalidTokenFormat.class, () -> convertToken(VALID_TOKEN_INVALID_VALUE));
    }

    @Test
    @DisplayName("Util - convertToken - Not Verified Email")
    void convertTokenNotVerifiedEmail() {
        assertThrows(EmailNotVerified.class, () -> convertToken(NOT_VERIFIED_EMAIL_TOKEN));
    }
}