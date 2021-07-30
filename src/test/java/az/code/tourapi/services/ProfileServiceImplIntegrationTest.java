package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;

import static az.code.tourapi.TourApiApplicationTests.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(PER_CLASS)
@AutoConfigureTestDatabase
@TestMethodOrder(OrderAnnotation.class)
class ProfileServiceImplIntegrationTest {

    public static final String SORT_BY = "status";

    @Autowired
    private ProfileService service;
    @MockBean
    private Clock clock;

    @Test
    @Order(1)
    @DisplayName("ProfileService - getRequests() - NEW_REQUEST")
    void getRequests_NEW_REQUEST() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.NEW_REQUEST,
                0, 100, SORT_BY)).hasSize(8)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.NEW_REQUEST);
    }

    @Test
    @Order(2)
    @DisplayName("ProfileService - getRequests() - OFFER_MADE")
    void getRequests_OFFER_MADE() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.OFFER_MADE,
                0, 100, SORT_BY)).hasSize(10)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.OFFER_MADE &&
                        userRequest.getOffer() != null);
    }

    @Test
    @Order(3)
    @DisplayName("ProfileService - getRequests() - ACCEPTED")
    void getRequests_ACCEPTED() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.ACCEPTED,
                0, 100, SORT_BY)).hasSize(10)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.ACCEPTED
                        && userRequest.getCustomer() != null && userRequest.getOffer() != null);
    }

    @Test
    @Order(4)
    @DisplayName("ProfileService - getRequests() - EXPIRED")
    void getRequests_EXPIRED() {
        assertThat(service.getRequests(AGENCY_NAME, null, UserRequestStatus.EXPIRED,
                0, 100, SORT_BY)).hasSize(2)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.EXPIRED);
    }

    @Test
    @Order(5)
    @DisplayName("ProfileService - getRequests() - isArchived")
    void getRequests_isArchived() {
        assertThat(service.getRequests(AGENCY_NAME, true, null,
                0, 100, SORT_BY)).hasSize(2)
                .filteredOn(userRequest -> userRequest.getStatus() == UserRequestStatus.EXPIRED &&
                        userRequest.isArchived());
    }

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public void init() throws SQLException {
        Clock fixedClock = Clock.fixed(DATE_TIME.atZone(SYSTEM_DEFAULT).toInstant(), SYSTEM_DEFAULT);
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("profile-service-sample-data.sql"));
        }
    }
}