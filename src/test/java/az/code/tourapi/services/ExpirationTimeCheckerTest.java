package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static az.code.tourapi.TourApiApplicationTests.DATE_TIME;
import static az.code.tourapi.TourApiApplicationTests.SYSTEM_DEFAULT;
import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_EXCHANGE;
import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_KEY;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ExpirationTimeChecker.class})
class ExpirationTimeCheckerTest {

    @MockBean
    RequestRepository requestRepo;
    @MockBean
    RabbitTemplate template;
    @MockBean
    Clock clock;
    @Captor
    ArgumentCaptor<LocalDateTime> time;

    @Autowired
    ExpirationTimeChecker checker;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(DATE_TIME.atZone(SYSTEM_DEFAULT).toInstant(), SYSTEM_DEFAULT);
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());
    }

    @Test
    @DisplayName("ExpirationTimeChecker - check()")
    void check() {
        List<String> expiredRequests = List.of(
                "f7d012b9-d2cd-4410-a869-d1bf52969c05", "c6df7d07-4bb3-497e-9770-957e1226f384",
                "f69f04cf-55c1-4d4a-a9ae-7723cd3b4b5f", "f5578d02-7b7d-470c-9b08-8f7eec4521ec"
        );
        LocalDateTime now = LocalDateTime.now(clock);
        when(requestRepo.getExpiredRequests(now)).thenReturn(expiredRequests);
        checker.check();
        verify(requestRepo).getExpiredRequests(time.capture());
        Assertions.assertEquals(now, time.getValue());
        verify(template, times(1))
                .convertAndSend(EXPIRATION_EXCHANGE, EXPIRATION_KEY, expiredRequests);
        verify(requestRepo, times(1))
                .changeStatusOfExpired(now);
    }
}