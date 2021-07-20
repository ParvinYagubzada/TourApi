package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.code.tourapi.utils.Specifications.sameStatus;
import static az.code.tourapi.utils.Specifications.sameValue;
import static az.code.tourapi.utils.Util.getResult;
import static az.code.tourapi.utils.Util.preparePage;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRequestRepository userRepo;

    @Override
    public List<UserRequest> getRequests(String agencyName, String username, Boolean isArchived,
                                         UserRequestStatus status, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = preparePage(pageNo, pageSize, sortBy);
        Page<UserRequest> pageResult = userRepo.findAll(sameStatus(status)
                .and(sameValue("agencyName", agencyName))
                .and(sameValue("username", username))
                .and(sameValue("isArchived", isArchived)), paging);
        return getResult(pageResult);
    }

    @Override
    public UserRequest getRequest(String agencyName, String username, String uuid) {
        return userRepo.findById(new UserRequest.UserRequestPK(username, agencyName, uuid))
                .orElseThrow(RuntimeException::new); //TODO: Custom Exception
    }
}
