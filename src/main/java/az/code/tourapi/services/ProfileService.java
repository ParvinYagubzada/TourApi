package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.UserRequest;

import java.util.List;

public interface ProfileService {

    List<UserRequest> getRequests(String agencyName, String username, Boolean isArchived, UserRequestStatus status,
                                  Integer pageNo, Integer pageSize, String sortBy);

    UserRequest getRequest(String agencyName, String username, String uuid);

    String archiveRequest(String agencyName, String username, String uuid);

    Offer makeOffer(String agencyName, String username, String uuid, OfferDTO dto);
}
