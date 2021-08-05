package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, String> {

    @Query("SELECT request.uuid FROM Request request " +
           "WHERE request.active = true " +
           "AND request.expirationTime <= :now")
    List<String> getExpiredRequests(LocalDateTime now);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value =
            "UPDATE requests SET status = false " +
            "WHERE status != false " +
            "AND expiration_time < :now")
    void changeStatusOfExpired(LocalDateTime now);
}
