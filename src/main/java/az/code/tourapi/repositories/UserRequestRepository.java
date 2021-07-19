package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRequestRepository extends JpaRepository<UserRequest, UserRequest.UserRequestPK>,
        JpaSpecificationExecutor<UserRequest> {
}
