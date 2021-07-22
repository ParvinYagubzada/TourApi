package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, String> {
}
