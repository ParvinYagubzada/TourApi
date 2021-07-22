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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
@SuppressWarnings("SpellCheckingInspection")
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
    @DisplayName("QueueListenerService - listenRequests")
    void listenRequests() {
        RawRequest request = RawRequest.builder()
                .uuid("87b2a6b9-e9d2-4008-be87-ee3a4df2bd72").language("AZ").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate("12.12.1212").travelEndDate("12.12.1213")
                .travellerCount("1 man 2 men").budget("123")
                .build();
        service.listenRequests(request);
        assertThat(requestRepo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("QueueListenerService - listenDeactivations")
    void listenDeactivations() {
        String uuid = "29353340-3152-4631-bd6a-b2b903428d7d";
        requestRepo.saveAndFlush(mappers.rawToRequest(new RawRequest(uuid, "RU", "Dark Land", "Taur-im-Duinath", "Tol Brandir", "23.01.2002", "14.06.1962", "025", "907"), LocalTime.parse("06:22:51.241456399")));
        service.listenDeactivations(uuid);
        Optional<Request> request = requestRepo.findById(uuid);
        assertTrue(request.isPresent());
        assertFalse(request.get().getIsActive());
    }

    @Test
    @DisplayName("QueueListenerService - listenAcceptances")
    void listenAcceptances() {
        String agencyName = "DataFlex";
        String uuid = "29353340-3152-4631-bd6a-b2b903428d7d";
        userRepo.saveAndFlush(User.builder().username("shayne.pfannerstill").agencyName(agencyName).voen("5344501174").email("serina.tremblay@yahoo.com").name("Cleveland Padberg").build());
        requestRepo.saveAndFlush(mappers.rawToRequest(new RawRequest(uuid, "RU", "Dark Land", "Taur-im-Duinath", "Tol Brandir", "23.01.2002", "14.06.1962", "025", "907"), LocalTime.parse("06:22:51.241456399")));
        AcceptedOffer offer = AcceptedOffer.builder()
                .uuid(uuid).agencyName(agencyName)
                .username("test").phoneNumber(null)
                .firstName("test1").lastName("test2")
                .userId("12345678")
                .build();
        service.listenAcceptances(offer);
        Optional<UserRequest> request = userRequestRepo.findById(new RequestId(agencyName, uuid));
        assertTrue(request.isPresent());
        assertEquals(mappers.acceptedToCustomer(offer), request.get().getCustomer());
    }

    @BeforeAll
    public void init(@Autowired DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("triggers.sql"));
        }
    }

    @AfterEach
    public void clean() {
        userRequestRepo.deleteAll();
        userRepo.deleteAll();
        requestRepo.deleteAll();
    }
}