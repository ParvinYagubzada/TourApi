package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.UserRequest;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;

public interface ProfileService {

    List<UserRequest> getRequests(String agencyName, Boolean isArchived, UserRequestStatus status,
                                  Integer pageNo, Integer pageSize, String sortBy);

    UserRequest getRequest(String agencyName, String uuid);

    UserRequest archiveRequest(String agencyName, String uuid);

    UserRequest unarchiveRequest(String agencyName, String uuid);

    UserRequest deleteRequest(String agencyName, String uuid);

    UserRequest makeOffer(String agencyName, String uuid, OfferDTO dto) throws IOException, JRException;
}
