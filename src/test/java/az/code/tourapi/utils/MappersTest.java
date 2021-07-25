package az.code.tourapi.utils;

import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.entities.*;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.security.AuthConfig;
import az.code.tourapi.security.SecurityServiceImpl;
import az.code.tourapi.utils.representations.MailRepresentation;
import az.code.tourapi.utils.representations.SimpleUserRepresentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static az.code.tourapi.utils.Mappers.timeFormatter;
import static az.code.tourapi.utils.Util.formatter;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
class MappersTest {

    @Autowired
    private Mappers mappers;
    @Autowired
    private AuthConfig config;

    @Test
    @DisplayName("Mappers - AcceptedOffer to CustomerInfo")
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

    @Test
    @DisplayName("Mappers - RegisterDTO to User")
    void registerToUser() {
        RegisterDTO dto = RegisterDTO.builder()
                .agencyName("test").voen("1234567890")
                .username("test1234").email("test@test.com")
                .name("test").surname("i_am_a_tester")
                .password("test123456").build();
        User expected = User.builder()
                .agencyName("test").voen("1234567890")
                .username("test1234").email("test@test.com")
                .name("test i_am_a_tester").build();
        assertEquals(expected, mappers.registerToUser(dto));
    }

    @Test
    @DisplayName("Mappers - OfferDTO to Offer")
    void dtoToOffer() {
        String agencyName = "test";
        String uuid = "a6056cf2-0be5-4742-b247-565a06a0a0d6";
        OfferDTO dto = OfferDTO.builder()
                .description("salary").travelDates("time")
                .price(386).notes("sock")
                .build();
        Offer expected = Offer.builder()
                .id(new RequestId(agencyName, uuid))
                .description("salary").travelDates("time")
                .price(386).notes("sock").isActive(true)
                .build();
        assertEquals(expected, mappers.dtoToOffer(dto, agencyName, uuid));
    }

    @Test
    @DisplayName("Mappers - AuthConfig to SecurityServiceImpl")
    void configToService(
            @Value("#{authConfig.authServerUrl}") String authServerUrl,
            @Value("#{authConfig.realm}") String realm,
            @Value("#{authConfig.clientId}") String clientId,
            @Value("#{authConfig.clientSecret}") String clientSecret,
            @Value("#{authConfig.mails}") Map<String, MailRepresentation> mails,
            @Value("#{authConfig.users}") Map<String, SimpleUserRepresentation> users
    ) {
        SimpleUserRepresentation admin = users.get("admin");
        SecurityServiceImpl expected = SecurityServiceImpl.builder()
                .role("app-user")
                .authServerUrl(authServerUrl).realm(realm).clientId(clientId).clientSecret(clientSecret)
                .adminUsername(admin.getUsername()).adminPassword(admin.getPassword())
                .mails(mails).build();
        assertEquals(expected, mappers.configToService(config));
    }

    @Test
    @DisplayName("Mappers - RawRequest to Request - In working hours")
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

    @Test
    @DisplayName("Mappers - RawRequest to Request - Before working hours")
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
    @DisplayName("Mappers - RawRequest to Request - After working hours")
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

    private Request.RequestBuilder createExpectedRequest(LocalDate start, LocalDate endDate) {
        return Request.builder()
                .uuid("test").language("test").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate(start).travelEndDate(endDate)
                .travellerCount("1 man 2 men").budget(123)
                .isActive(true);
    }
}