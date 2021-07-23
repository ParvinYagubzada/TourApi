package az.code.tourapi.services;

import az.code.tourapi.repositories.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpirationTimeChecker {

    private final RequestRepository requestRepo;

    @Scheduled(fixedDelayString = "${app.expiration-check-duration}")
    public void check() {
        requestRepo.changeStatusOfExpired();
    }
}
