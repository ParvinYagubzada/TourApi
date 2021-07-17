package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRequestRepository extends JpaRepository<UserRequest, UserRequest.UserRequestPK> {

    Page<UserRequest> findAllByCompanyNameAndUsername(String companyName, String username, Pageable pageable);

}
