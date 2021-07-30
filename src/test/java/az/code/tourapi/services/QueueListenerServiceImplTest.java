package az.code.tourapi.services;

import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.repositories.RequestRepository;
import az.code.tourapi.repositories.UserRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;

import static az.code.tourapi.TourApiApplicationTests.TEST_STRING;
import static az.code.tourapi.TourApiApplicationTests.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@TestInstance(PER_CLASS)
@SuppressWarnings("SpellCheckingInspection")
@Sql(scripts = "classpath:truncate-data.sql", executionPhase = AFTER_TEST_METHOD)
class QueueListenerServiceImplTest {

    @Autowired
    private QueueListenerService service;
    @Autowired
    private Mappers mappers;
    @Autowired
    private RequestRepository requestRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserRequestRepository userRequestRepo;

    @Test
    @DisplayName("QueueListenerService - listenRequests()")
    void listenRequests() {
        RawRequest request = RawRequest.builder()
                .uuid(UUID).language("AZ").tourType(TEST_STRING)
                .addressTo(TEST_STRING).addressFrom(TEST_STRING)
                .travelStartDate("12.12.1212").travelEndDate("12.12.1213")
                .travellerCount("1 man 2 men").budget("123")
                .build();
        service.listenRequests(request);
        assertThat(requestRepo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("QueueListenerService - listenDeactivations()")
    void listenDeactivations() {
        requestRepo.saveAndFlush(mappers.rawToRequest(new RawRequest(UUID, "RU", "Dark Land", "Taur-im-Duinath", "Tol Brandir", "23.01.2002", "14.06.1962", "025", "907"), LocalTime.parse("06:22:51.241456399")));
        service.listenDeactivations(UUID);
        Optional<Request> request = requestRepo.findById(UUID);
        assertTrue(request.isPresent());
        assertFalse(request.get().isActive());
    }

    @Test
    @DisplayName("QueueListenerService - listenAcceptances()")
    void listenAcceptances() {
        String agencyName = "DataFlex";
        userRepo.saveAndFlush(User.builder().username("shayne.pfannerstill").agencyName(agencyName).voen("5344501174").email("serina.tremblay@yahoo.com").name("Cleveland Padberg").build());
        requestRepo.saveAndFlush(mappers.rawToRequest(new RawRequest(UUID, "RU", "Dark Land", "Taur-im-Duinath", "Tol Brandir", "23.01.2002", "14.06.1962", "025", "907"), LocalTime.parse("06:22:51.241456399")));
        AcceptedOffer offer = AcceptedOffer.builder()
                .uuid(UUID).agencyName(agencyName)
                .username(TEST_STRING).phoneNumber(null)
                .firstName("test1").lastName("test2")
                .userId("12345678")
                .build();
        service.listenAcceptances(offer);
        Optional<UserRequest> request = userRequestRepo.findById(new RequestId(agencyName, UUID));
        assertTrue(request.isPresent());
        assertEquals(mappers.acceptedToCustomer(offer), request.get().getCustomer());
    }

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public void init() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("triggers.sql"));
        }
    }

    @AfterAll
    public void cleanTriggers() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("drop-triggers.sql"));
        }
    }
}