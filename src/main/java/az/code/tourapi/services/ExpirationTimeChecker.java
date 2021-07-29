package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static az.code.tourapi.configurations.RabbitConfig.*;

@Component
@RequiredArgsConstructor
public class ExpirationTimeChecker {

    private final RequestRepository requestRepo;
    private final RabbitTemplate template;

    @Scheduled(fixedDelayString = "${app.expiration-check-duration}")
    public void check() {
        List<String> expiredRequests = requestRepo.getExpiredRequests();
        if (expiredRequests.size() != 0) {
            template.convertAndSend(EXPIRATION_EXCHANGE, EXPIRATION_KEY, expiredRequests);
            requestRepo.changeStatusOfExpired();
        }
    }
}
