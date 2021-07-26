package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.MultipleOffers;
import az.code.tourapi.exceptions.OutOfWorkingHours;
import az.code.tourapi.exceptions.RequestExpired;
import az.code.tourapi.exceptions.RequestNotFound;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Optional;

import static az.code.tourapi.TourApiApplicationTests.*;
import static az.code.tourapi.utils.Mappers.timeFormatter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Clock clock;

    private ProfileServiceImpl service;

    @BeforeEach
    public void init() {
        service = new ProfileServiceImpl(template, userRepo, offerRepo, mappers, clock);
        service.setStartTimeString("09:00:00");
        service.setEndTimeString("19:00:00");
    }

    @Test
    @DisplayName("ProfileService - getRequest - Valid")
    void getRequest() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(new UserRequest()));
        assertEquals(new UserRequest(), service.getRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - getRequest - RequestNotFound")
    void getRequest_RequestNotFound() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.getRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - archiveRequest - Valid")
    void archiveRequest() {
        UserRequest expected = UserRequest.builder().isArchived(true).build();
        UserRequest param = UserRequest.builder().isArchived(false).build();
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(param));
        Mockito.when(userRepo.save(expected))
                .thenReturn(expected);
        assertEquals(expected, service.archiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - archiveRequest - RequestNotFound")
    void archiveRequest_RequestNotFound() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.archiveRequest(AGENCY_NAME, UUID));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - Valid")
    void makeOffer() throws IOException {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        OfferDTO dto = OfferDTO.builder()
                .description("salary").travelDates("time")
                .price(386).notes("sock")
                .build();
        Offer offer = Offer.builder()
                .id(new RequestId(AGENCY_NAME, UUID))
                .description("salary").travelDates("time")
                .price(386).notes("sock").isActive(true)
                .build();
        UserRequest expected = UserRequest.builder().offer(offer).status(UserRequestStatus.OFFER_MADE).build();
        UserRequest param = UserRequest.builder().status(UserRequestStatus.NEW_REQUEST).build();
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(param));
        Mockito.when(offerRepo.existsById(ID))
                .thenReturn(false);
        Mockito.when(userRepo.save(expected))
                .thenReturn(expected);
        Mockito.when(mappers.dtoToOffer(dto, AGENCY_NAME, UUID))
                .thenReturn(offer);
        assertEquals(expected, service.makeOffer(AGENCY_NAME, UUID, dto));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - OutOfWorkingHours")
    void makeOffer_OutOfWorkingHours() {
        mockTime(Clock.fixed(getFixedInstant("01:00:00"), SYSTEM_DEFAULT));
        assertThrows(OutOfWorkingHours.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - RequestNotFound")
    void makeOffer_RequestNotFound() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - RequestExpired")
    void makeOffer_RequestExpired() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().status(UserRequestStatus.EXPIRED).build()));
        assertThrows(RequestExpired.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - MultipleOffers")
    void makeOffer_MultipleOffers() {
        mockTime(Clock.fixed(getFixedInstant("15:00:00"), SYSTEM_DEFAULT));
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().status(UserRequestStatus.NEW_REQUEST).build()));
        Mockito.when(offerRepo.existsById(ID))
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