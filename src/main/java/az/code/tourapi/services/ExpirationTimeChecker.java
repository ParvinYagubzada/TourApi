package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_EXCHANGE;
import static az.code.tourapi.configurations.RabbitConfig.EXPIRATION_KEY;

@Component
@RequiredArgsConstructor
public class ExpirationTimeChecker {

    private final RequestRepository requestRepo;
    private final RabbitTemplate template;
    private final Clock clock;

    @Scheduled(fixedDelayString = "${app.expiration-check-duration}")
    public void check() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<String> expiredRequests = requestRepo.getExpiredRequests(now);
        if (expiredRequests.size() != 0) {
            template.convertAndSend(EXPIRATION_EXCHANGE, EXPIRATION_KEY, expiredRequests);
            requestRepo.changeStatusOfExpired(now);
        }
    }
}
