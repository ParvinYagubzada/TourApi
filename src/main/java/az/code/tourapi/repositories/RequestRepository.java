package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RequestRepository extends JpaRepository<Request, String> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE requests SET status = FALSE " +
            "WHERE uuid = :uuid")
    void deactivate(String uuid);
}
