package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_EXCHANGE;
import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_KEY;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Config.class})
class ExpirationTimeCheckerTest {

    @MockBean
    RequestRepository requestRepo;
    @MockBean
    RabbitTemplate template;

    @Autowired
    ExpirationTimeChecker checker;

    @Test
    void check() {
        List<String> expiredRequests = List.of(
                "f7d012b9-d2cd-4410-a869-d1bf52969c05",
                "c6df7d07-4bb3-497e-9770-957e1226f384",
                "f69f04cf-55c1-4d4a-a9ae-7723cd3b4b5f"
        );
        when(requestRepo.getExpiredRequests()).thenReturn(expiredRequests);
        checker.check();
        verify(template, times(1))
                .convertAndSend(EXPIRATION_EXCHANGE, EXPIRATION_KEY, expiredRequests);
        verify(requestRepo, times(1))
                .changeStatusOfExpired();
    }
}