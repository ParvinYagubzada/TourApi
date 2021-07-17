package az.code.tourapi.services;

import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.repositories.RequestRepository;
import az.code.tourapi.utils.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {

    public static final String REQUEST_QUEUE = "requestQueue";
    public static final String STOP_QUEUE = "stopQueue";
    public static final String ACCEPTED_QUEUE = "acceptQueue";

    private final RequestRepository requestRepo;
    private final Mappers mappers;

    @RabbitListener(queues = REQUEST_QUEUE)
    public void listenRequest(RawRequest data) {
        requestRepo.save(mappers.rawToRequest(data));
    }

    @RabbitListener(queues = STOP_QUEUE)
    public void listenRequest(String uuid) {
        requestRepo.deactivate(uuid);
    }


}
