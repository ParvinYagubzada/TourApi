package az.code.tourapi.repositories;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static az.code.tourapi.TourApiApplicationTests.EXPIRED_REQUEST_UUIDS;
import static az.code.tourapi.TourApiApplicationTests.SYSTEM_DEFAULT;
import static java.time.Month.AUGUST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@TestInstance(PER_CLASS)
@DataJpaTest(showSql = false)
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "classpath:request-repo-sample-data.sql", executionPhase = BEFORE_TEST_METHOD)
class RequestRepositoryTest {

    @Autowired
    private RequestRepository requestRepo;
    @MockBean
    private Clock clock;

    private LocalDateTime now;

    @BeforeAll
    void setUp() {
        Clock fixedClock = Clock.fixed(LocalDate
                        .of(2021, AUGUST, 1).atStartOfDay().atZone(SYSTEM_DEFAULT).toInstant(),
                SYSTEM_DEFAULT);
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
        now = LocalDateTime.now(clock);
    }

    @Test
    @DisplayName("RequestRepository - getExpiredRequests()")
    void getExpiredRequests() {
        assertThat(requestRepo.getExpiredRequests(now))
                .hasSize(10).containsExactlyElementsOf(EXPIRED_REQUEST_UUIDS);
    }

    @Test
    @DisplayName("RequestRepository - changeStatusOfExpired()")
    void changeStatusOfExpired() {
        requestRepo.changeStatusOfExpired(now);
        assertThat(requestRepo.findAllById(EXPIRED_REQUEST_UUIDS))
                .filteredOn(request -> !request.isActive())
                .hasSize(10);
    }
}