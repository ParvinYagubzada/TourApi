package az.code.tourapi.repositories;


import az.code.tourapi.models.entities.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, String> {
    Optional<Verification> findByTokenAndUser_Username(String token, String username);
}
