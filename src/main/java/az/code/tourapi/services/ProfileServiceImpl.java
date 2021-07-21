package az.code.tourapi.services;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.MultipleOffers;
import az.code.tourapi.exceptions.RequestExpired;
import az.code.tourapi.exceptions.RequestNotFound;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.repositories.OfferRepository;
import az.code.tourapi.repositories.UserRequestRepository;
import az.code.tourapi.utils.Mappers;
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
    private final OfferRepository offerRepo;
    private final Mappers mappers;

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
                .orElseThrow(RequestNotFound::new);
    }

    @Override
    public String archiveRequest(String agencyName, String username, String uuid) {
        userRepo.archive(agencyName, username, uuid);
        return uuid;
    }

    @Override
    public Offer makeOffer(String agencyName, String username, String uuid, OfferDTO dto) {
        if (userRepo.isExpired(agencyName, username, uuid, UserRequestStatus.EXPIRED.ordinal()))
            throw new RequestExpired();
        if (offerRepo.existsById(new Offer.OfferPK(agencyName, uuid)))
            throw new MultipleOffers();
        Offer offer = mappers.dtoToOffer(dto, agencyName, uuid);
        offerRepo.save(offer);
        userRepo.setOffer(agencyName, username, uuid);
        return offer;
    }
}
