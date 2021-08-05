package az.code.tourapi.services;

import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;

public interface QueueListenerService {
    void listenRequests(RawRequest data);

    void listenDeactivations(String uuid);

    void listenAcceptances(AcceptedOffer acceptedOffer);
}
