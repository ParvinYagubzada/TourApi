package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRequestRepository extends JpaRepository<UserRequest, UserRequest.UserRequestPK>,
        JpaSpecificationExecutor<UserRequest> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE user_requests SET customer_id = :customerId " +
            "WHERE agency_name = :agencyName AND uuid = :uuid")
    void setCustomer(String agencyName, String uuid, String customerId);
}
