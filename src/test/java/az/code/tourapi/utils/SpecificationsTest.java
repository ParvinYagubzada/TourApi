package az.code.tourapi.utils;

import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.UserRequestRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static az.code.tourapi.enums.UserRequestStatus.NEW_REQUEST;
import static az.code.tourapi.models.entities.UserRequest.Fields.archived;
import static az.code.tourapi.models.entities.UserRequest.Fields.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@SpringBootTest
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class SpecificationsTest {

    @Autowired
    private UserRequestRepository userRequestRepo;

    @Test
    @DisplayName("Specifications - sameValueWithId() - agencyName")
    void sameValueWithId() {
        String agencyName = "DataFlex";
        List<UserRequest> requests = userRequestRepo.findAll(Specifications
                .sameValueWithId(RequestId.Fields.agencyName, agencyName));
        assertThat(requests).hasSize(30)
                .filteredOn(userRequest -> userRequest.getId().getAgencyName().equals(agencyName))
                .hasSize(30);
    }

    @Test
    @DisplayName("Specifications - sameValue() - status")
    void sameValue_status() {
        List<UserRequest> requests = userRequestRepo.findAll(Specifications
                .sameValue(status, NEW_REQUEST));
        assertThat(requests).hasSize(600)
                .filteredOn(userRequest -> userRequest.getStatus() == NEW_REQUEST)
                .hasSize(600);
    }

    @Test
    @DisplayName("Specifications - sameValue() - isArchived")
    void sameValue_isArchived() {
        List<UserRequest> requests = userRequestRepo.findAll(Specifications
                .sameValue(archived, false));
        assertThat(requests).hasSize(600)
                .filteredOn(userRequest -> !userRequest.isArchived())
                .hasSize(600);
    }

    @Autowired
    DataSource dataSource;

    @BeforeAll
    public void init() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("specifications-sample-data.sql"));
        }
    }

    @AfterAll
    public void clean() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("truncate-data.sql"));
        }
    }
}