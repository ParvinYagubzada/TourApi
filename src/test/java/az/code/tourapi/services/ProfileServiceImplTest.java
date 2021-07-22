package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.MultipleOffers;
import az.code.tourapi.exceptions.RequestExpired;
import az.code.tourapi.exceptions.RequestNotFound;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodName.class)
@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    public static final String UUID = "c64808f2-8682-4a7c-91c7-16d4201438b3";
    public static final String AGENCY_NAME = "Global Travel";
    public static final RequestId ID = new RequestId(AGENCY_NAME, UUID);

    @Mock
    private UserRequestRepository userRepo;
    @Mock
    private OfferRepository offerRepo;
    @Mock
    private Mappers mappers;

    private ProfileService service;

    @BeforeEach
    void init() {
        service = new ProfileServiceImpl(userRepo, offerRepo, mappers);
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
    void makeOffer() {
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
    @DisplayName("ProfileService - makeOffer - RequestNotFound")
    void makeOffer_RequestNotFound() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(RequestNotFound.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - RequestExpired")
    void makeOffer_RequestExpired() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().status(UserRequestStatus.EXPIRED).build()));
        assertThrows(RequestExpired.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }

    @Test
    @DisplayName("ProfileService - makeOffer - MultipleOffers")
    void makeOffer_MultipleOffers() {
        Mockito.when(userRepo.findById(ID))
                .thenReturn(Optional.of(UserRequest.builder().status(UserRequestStatus.NEW_REQUEST).build()));
        Mockito.when(offerRepo.existsById(ID))
                .thenReturn(true);
        assertThrows(MultipleOffers.class, () -> service.makeOffer(AGENCY_NAME, UUID, null));
    }
}