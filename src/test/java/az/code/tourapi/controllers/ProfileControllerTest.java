package az.code.tourapi.controllers;

import az.code.tourapi.exceptions.MultipleOffers;
import az.code.tourapi.exceptions.RequestExpired;
import az.code.tourapi.exceptions.RequestNotFound;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.services.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static az.code.tourapi.enums.UserRequestStatus.NEW_REQUEST;
import static az.code.tourapi.enums.UserRequestStatus.OFFER_MADE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpellCheckingInspection")
@TestInstance(PER_CLASS)
@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class ProfileControllerTest {

    public static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType
            (APPLICATION_JSON.getType(), APPLICATION_JSON.getSubtype(), UTF_8);

    public static final String BASE_URL = "/api/v1/profile";
    public static final String AUTHORIZATION = "Authorization";
    public static final String AGENCY_NAME = "Global Travel";
    public static final String UUID = "a46d6230-c521-46ba-9957-1bb0347370e7";
    public static final String TOKEN = ".eyJleHAiOjE2MjY5OTg2MDQsImlhdCI6MTYyNjk2MjYwNCwianRpIjoiZmJlOTdmZDYtNTNlMC00MGZkLWFkMzItZDExOTIwNjI3NjFmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMmFkYTk2ZDAtNmExMC00ZThjLTg2YmQtMzQzOGE3Zjk2OWVmIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiN2I1NGFhMjktMWE3Yy00NDFkLWIzMDItMTNiZDQyZmIzZTFjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiLCJkZWZhdWx0LXJvbGVzLXRvdXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0b3VyLWFwcCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjcmVhdGlvbl90aW1lIjoxNjI2OTYxOTQwOTI2LCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYWdlbmN5X25hbWUiOiJHbG9iYWwgVHJhdmVsIiwidm9lbiI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiUGVydmluIFlhcXViemFkZSIsInByZWZlcnJlZF91c2VybmFtZSI6InBlcnZpbnVzZXIiLCJnaXZlbl9uYW1lIjoiUGVydmluIiwiZmFtaWx5X25hbWUiOiJZYXF1YnphZGUiLCJlbWFpbCI6InBhcnZpbnl5QGNvZGUuZWR1LmF6In0.";
    public static final LocalDate DATE = LocalDate.now();
    public static final LocalDateTime DATE_TIME = LocalDateTime.now();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfileService profileService;

    @BeforeAll
    void start() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    @DisplayName("ProfileController - getRequests() - Valid")
    void getRequests() throws Exception {
        List<UserRequest> requests = IntStream.range(0, 4).mapToObj(value -> new UserRequest()).collect(Collectors.toList());
        when(profileService.getRequests(AGENCY_NAME, null, null, 0, 4, "status"))
                .thenReturn(requests);
        mockMvc
                .perform(get(BASE_URL + "/requests", UUID)
                        .param("pageSize", "4")
                        .header(AUTHORIZATION, TOKEN))
                .andExpect(content().string(mapper.writeValueAsString(requests)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ProfileController - getRequest() - Valid")
    void getRequest() throws Exception {
        Request request = new Request(UUID, "RU", "relaxing", "tural_offer", "Bkk", DATE, DATE, "206", 457, true, DATE_TIME, DATE_TIME);
        UserRequest response = UserRequest.builder().id(new RequestId(AGENCY_NAME, UUID)).request(request).status(NEW_REQUEST).isArchived(false).build();
        when(profileService.getRequest(AGENCY_NAME, UUID)).thenReturn(response);
        mockMvc
                .perform(get(BASE_URL + "/requests/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ProfileController - archiveRequest() - Valid")
    void archiveRequest() throws Exception {
        UserRequest response = UserRequest.builder().isArchived(true).build();
        when(profileService.archiveRequest(AGENCY_NAME, UUID)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/archive/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isAccepted());
    }

    @Test
    @DisplayName("ProfileController - archiveRequest() - NOT FOUND")
    void archiveRequest_RequestNotFound() throws Exception {
        when(profileService.archiveRequest(AGENCY_NAME, UUID)).thenThrow(new RequestNotFound());
        mockMvc
                .perform(post(BASE_URL + "/archive/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN))
                .andExpect(content().string("This request does not exists."))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("ProfileController - createOffer() - Valid")
    void createOffer() throws Exception {
        RequestId id = new RequestId(AGENCY_NAME, UUID);
        Request request = new Request(UUID, "RU", "relaxing", "tural_offer", "Bkk", DATE, DATE, "206", 457, true, DATE_TIME, DATE_TIME);
        Offer offer = new Offer(id, "test", "test", 1, "test", true, DATE_TIME);
        OfferDTO dto = new OfferDTO("test", "test", 1, "test");
        UserRequest response = new UserRequest(id, OFFER_MADE, false, request, null, offer);

        when(profileService.makeOffer(AGENCY_NAME, UUID, dto)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/makeOffer/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJson(dto)))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("ProfileController - createOffer() - CONFLICT")
    void createOffer_RequestExpired() throws Exception {
        OfferDTO dto = new OfferDTO("test", "test", 1, "test");

        when(profileService.makeOffer(AGENCY_NAME, UUID, dto)).thenThrow(new RequestExpired());
        mockMvc
                .perform(post(BASE_URL + "/makeOffer/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJson(dto)))
                .andExpect(content().string("This request is expired."))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("ProfileController - createOffer() - NOT ACCEPTABLE")
    void createOffer_MultipleOffers() throws Exception {
        OfferDTO dto = new OfferDTO("test", "test", 1, "test");

        when(profileService.makeOffer(AGENCY_NAME, UUID, dto)).thenThrow(new MultipleOffers());
        mockMvc
                .perform(post(BASE_URL + "/makeOffer/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJson(dto)))
                .andExpect(content().string("You can't have more than one offer."))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("ProfileController - createOffer() - INTERNAL SERVER ERROR")
    void createOffer_IOException() throws Exception {
        OfferDTO dto = new OfferDTO("test", "test", 1, "test");

        when(profileService.makeOffer(AGENCY_NAME, UUID, dto)).thenThrow(new IOException());
        mockMvc
                .perform(post(BASE_URL + "/makeOffer/{uuid}", UUID)
                        .header(AUTHORIZATION, TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(getJson(dto)))
                .andExpect(status().isInternalServerError());
    }

    private <T> String getJson(T dto) throws JsonProcessingException {
        return mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);
    }
}