package az.code.tourapi.services;

import az.code.tourapi.models.entities.CustomerInfo;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.repositories.CustomerRepository;
import az.code.tourapi.repositories.RequestRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {

    public static final String REQUEST_QUEUE = "requestQueue";
    public static final String STOP_QUEUE = "stopQueue";
    public static final String ACCEPTED_QUEUE = "acceptQueue";

    private final RequestRepository requestRepo;
    private final CustomerRepository customerRepo;
    private final UserRequestRepository userRepo;
    private final Mappers mappers;

    @RabbitListener(queues = REQUEST_QUEUE)
    public void listenRequests(RawRequest data) {
        requestRepo.save(mappers.rawToRequest(data, LocalTime.now()));
    }

    @RabbitListener(queues = STOP_QUEUE)
    public void listenDeactivations(String uuid) {
        requestRepo.deactivate(uuid);
    }

    @RabbitListener(queues = ACCEPTED_QUEUE)
    public void listenAcceptances(AcceptedOffer acceptedOffer) {
        CustomerInfo info = mappers.acceptedToCustomer(acceptedOffer);
        customerRepo.save(info);
        userRepo.setCustomer(acceptedOffer.getAgencyName(), acceptedOffer.getUuid(), info.getUsername());
    }
}
