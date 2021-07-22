package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.RequestId;
import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRequestRepository extends JpaRepository<UserRequest, RequestId>,
        JpaSpecificationExecutor<UserRequest> {

//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true, value =
//            "UPDATE user_requests SET is_archived = true " +
//            "WHERE agency_name = :agencyName AND uuid = :uuid")
//    void archive(String agencyName, String uuid);

}
