package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.MultipleOffers;
import az.code.tourapi.exceptions.RequestExpired;
import az.code.tourapi.exceptions.RequestNotFound;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static az.code.tourapi.utils.Specifications.sameValue;
import static az.code.tourapi.utils.Specifications.sameValueWithId;
import static az.code.tourapi.utils.Util.getResult;
import static az.code.tourapi.utils.Util.preparePage;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRequestRepository userRepo;
    private final OfferRepository offerRepo;
    private final Mappers mappers;

    @Override
    public List<UserRequest> getRequests(String agencyName, Boolean isArchived,
                                         UserRequestStatus status, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = preparePage(pageNo, pageSize, sortBy);
        Page<UserRequest> pageResult = userRepo.findAll(sameValue(UserRequest.Fields.status, status)
                .and(sameValueWithId(RequestId.Fields.agencyName, agencyName))
                .and(sameValue(UserRequest.Fields.isArchived, isArchived)), paging);
        return getResult(pageResult);
    }

    @Override
    public UserRequest getRequest(String agencyName, String uuid) {
        return userRepo.findById(new RequestId(agencyName, uuid))
                .orElseThrow(RequestNotFound::new);
    }

    @Override
    public UserRequest archiveRequest(String agencyName, String uuid) {
        RequestId id = new RequestId(agencyName, uuid);
        UserRequest userRequest = userRepo.findById(id)
                .orElseThrow(RequestNotFound::new);
        return userRepo.save(userRequest.setArchived(true));
    }

    @Override
    public UserRequest makeOffer(String agencyName, String uuid, OfferDTO dto) {
        RequestId id = new RequestId(agencyName, uuid);
        UserRequest userRequest = userRepo.findById(id)
                .orElseThrow(RequestNotFound::new);
        if (userRequest.getStatus() == UserRequestStatus.EXPIRED)
            throw new RequestExpired();
        if (offerRepo.existsById(id))
            throw new MultipleOffers();
        return userRepo.save(userRequest.setOffer(mappers.dtoToOffer(dto, agencyName, uuid)));
    }
}
