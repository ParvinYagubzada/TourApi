package az.code.tourapi.repositories;


import az.code.tourapi.models.entities.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, String> {
}
