package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.*;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;

import static az.code.tourapi.TourApiApplicationTests.*;
import static az.code.tourapi.enums.UserRequestStatus.EXPIRED;
import static az.code.tourapi.utils.Util.timeFormatter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodName.class)
@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    public static final RequestId ID = new RequestId(AGENCY_NAME, UUID);

    @Mock
    private UserRequestRepository userRepo;
    @Mock
    private OfferRepository offerRepo;
    @Mock
    private Mappers mappers;
    @Mock
    private RabbitTemplate template;
    @Mock
    private JasperService jasperService;
    @Mock
    private Clock clock;

    @InjectMocks
    private ProfileServiceImpl service;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(service, "startTimeString", "09:00:00");
        ReflectionTestUtils.setField(service, "endTimeString", "19:00:00");
    }

    @Test
    @DisplayName("ProfileService - getRequest() - Valid")
    void getRequest() {
        when(userRepo.findById(ID))
                .thenReturn(Optional.of(new UserRequest()));
        assertEquals(new UserRequest(), service.getRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - getRequest() - RequestNotFound")
    void getRequest_RequestNotFound() {
        when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.getRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - archiveRequest() - Valid")
    void archiveRequest() {
        UserRequest expected = UserRequest.builder().archived(true).build();
        UserRequest param = UserRequest.builder().archived(false).build();
        mockFindAndSave(expected, param);
        assertEquals(expected, service.archiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - archiveRequest() - RequestNotFound")
    void archiveRequest_RequestNotFound() {
        when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.archiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - unarchiveRequest() - Valid")
    void unarchiveRequest() {
        UserRequest expected = UserRequest.builder().archived(false).build();
        UserRequest param = UserRequest.builder().archived(true).build();
        mockFindAndSave(expected, param);
        assertEquals(expected, service.unarchiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - unarchiveRequest() - InvalidUnarchive")
    void unarchiveRequest_InvalidUnarchive() {
        UserRequest param = UserRequest.builder().status(EXPIRED).archived(true).build();
        when(userRepo.findById(ID))
                .thenReturn(Optional.of(param));
        assertThrows(InvalidUnarchive.class, () -> service.unarchiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - deleteRequest() - Valid")
    void deleteRequest() {
        UserRequest expected = UserRequest.builder().deleted(true).build();
        UserRequest param = UserRequest.builder().deleted(false).build();
        mockFindAndSave(expected, param);
        assertEquals(expected, service.deleteRequest(AGENCY_NAME, UUID));
    }

    private void mockFindAndSave(UserRequest expected, UserRequest param) {
        when(userRepo.findById(ID))
                .thenReturn(Optional.of(param));
        when(userRepo.save(expected))
                .thenReturn(expected);
    }

    @Test
    @DisplayName("ProfileService - makeOffer() - Valid")
    void makeOffer() throws IOException, JRException {
        OfferDTO dto = OfferDTO.builder()
                .description("salary").travelDates("time")
                .price(386).notes("sock")
                .build();
        Offer offer = Offer.builder()
                .id(new RequestId(AGENCY_NAME, UUID))
                .description("salary").travelDates("time")
                .price(386).notes("sock").isActive(true)
                .build();
        UserRequest expected = UserRequest.builder()
                .request(Request.builder().active(true).build())
                .offer(offer).status(UserRequestStatus.OFFER_MADE)
                .build();
        UserRequest param = UserRequest.builder()
                .request(Request.builder().active(true).build())
                .build();
        File testFile = ResourceUtils.getFile("src/test/resources/test.test");
        assertTrue(testFile.createNewFile());

        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        when(userRepo.findById(ID)).thenReturn(Optional.of(param));
        when(offerRepo.existsById(ID)).thenReturn(false);
        when(jasperService.generateImage(dto)).thenReturn(testFile);
        when(userRepo.save(expected)).thenReturn(expected);
        when(mappers.dtoToOffer(dto, AGENCY_NAME, UUID)).thenReturn(offer);

        assertEquals(expected, service.makeOffer(AGENCY_NAME, UUID, dto));
    }

    @Test
    @DisplayName("ProfileService - makeOffer() - OutOfWorkingHours")
    void makeOffer_OutOfWorkingHours() {
        mockTime(Clock.fixed(getFixedInstant("01:00:00"), SYSTEM_DEFAULT));
        assertThrows(OutOfWorkingHours.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer() - RequestNotFound")
    void makeOffer_RequestNotFound() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer() - RequestExpired")
    void makeOffer_RequestExpired() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().request(Request.builder().active(false).build()).build()));
        assertThrows(RequestExpired.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer() - MultipleOffers")
    void makeOffer_MultipleOffers() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().request(Request.builder().active(true).build()).build()));
        when(offerRepo.existsById(ID))
                .thenReturn(true);
        assertThrows(MultipleOffers.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    private Instant getFixedInstant(String timeString) {
        return DATE.atTime(LocalTime.parse(timeString, timeFormatter)).atZone(SYSTEM_DEFAULT).toInstant();
    }

    private void mockTime(Clock fixedClock) {
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }
}