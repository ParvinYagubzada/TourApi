package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RequestRepository extends JpaRepository<Request, String> {
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value =
            "UPDATE requests SET status = false " +
            "WHERE now() > expiration_time")
    void changeStatusOfExpired();
}
