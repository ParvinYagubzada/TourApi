package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.entities.UserRequest;

import java.util.List;

public interface ProfileService {

    List<UserRequest> getRequests(String companyName, String username, Boolean isArchived, UserRequestStatus status,
                                  Integer pageNo, Integer pageSize, String sortBy);

    UserRequest getRequest(String companyName, String username, String uuid);
}
