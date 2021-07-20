package az.code.tourapi.utils;

import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.entities.CustomerInfo;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static az.code.tourapi.utils.Mappers.timeFormatter;
import static az.code.tourapi.utils.Util.formatter;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MappersTest {

    @Autowired
    Mappers mappers;

    @Test
    void registerToUser() {
        RegisterDTO dto = RegisterDTO.builder()
                .agencyName("test").voen(1234567890).username("test1234")
                .name("test").surname("i_am_a_tester").email("test@test.com")
                .password("test123456").build();
        User expected = User.builder()
                .agencyName("test").voen(1234567890)
                .username("test1234").email("test@test.com")
                .name("test i_am_a_tester").build();
        assertEquals(expected, mappers.registerToUser(dto));
    }

    @Test
    void rawToRequestWorkingHours() {
        LocalDate start = LocalDate.parse("12.12.1212", formatter);
        LocalDate endDate = LocalDate.parse("12.12.1213", formatter);
        LocalTime now = LocalTime.parse("10:00:00", timeFormatter);

        RawRequest rawRequest = createRawRequest();
        Request expected = createExpectedRequest(start, endDate)
                .expirationTime(LocalDate.now().atTime(now).plusHours(8))
                .build();
        assertEquals(expected, mappers.rawToRequest(rawRequest, now));
    }

    private Request.RequestBuilder createExpectedRequest(LocalDate start, LocalDate endDate) {
        return Request.builder()
                .uuid("test").language("test").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate(start).travelEndDate(endDate)
                .travellerCount("1 man 2 men").budget(123)
                .isActive(true);
    }

    @Test
    void rawToRequestBeforeWorkingHours() {
        LocalDate start = LocalDate.parse("12.12.1212", formatter);
        LocalDate endDate = LocalDate.parse("12.12.1213", formatter);
        LocalTime now = LocalTime.parse("08:00:00", timeFormatter);
        LocalTime begin = LocalTime.parse("09:00:00", timeFormatter);

        RawRequest rawRequest = createRawRequest();
        Request expected = createExpectedRequest(start, endDate)
                .expirationTime(LocalDate.now().atTime(begin).plusHours(8))
                .build();
        assertEquals(expected, mappers.rawToRequest(rawRequest, now));
    }

    @Test
    void rawToRequestAfterWorkingHours() {
        LocalDate start = LocalDate.parse("12.12.1212", formatter);
        LocalDate endDate = LocalDate.parse("12.12.1213", formatter);
        LocalTime now = LocalTime.parse("20:00:00", timeFormatter);
        LocalTime begin = LocalTime.parse("09:00:00", timeFormatter);

        RawRequest rawRequest = createRawRequest();
        Request expected = createExpectedRequest(start, endDate)
                .expirationTime(LocalDate.now().plusDays(1).atTime(begin).plusHours(8))
                .build();
        assertEquals(expected, mappers.rawToRequest(rawRequest, now));
    }

    private RawRequest createRawRequest() {
        return RawRequest.builder()
                .uuid("test").language("test").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate("12.12.1212").travelEndDate("12.12.1213")
                .travellerCount("1 man 2 men").budget("123")
                .build();
    }

    @Test
    void acceptedToCustomer() {
        AcceptedOffer offer = AcceptedOffer.builder()
                .uuid("1234").agencyName("test")
                .username("test").phoneNumber(null)
                .firstName("test1").lastName("test2")
                .userId("12345678")
                .build();
        CustomerInfo expected = CustomerInfo.builder()
                .username("test").phoneNumber(null)
                .firstName("test1").lastName("test2")
                .userId("12345678")
                .build();
        assertEquals(expected, mappers.acceptedToCustomer(offer));
    }
}