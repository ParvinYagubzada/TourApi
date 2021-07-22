package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Offer;
import az.code.tourapi.models.entities.RequestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, RequestId> {
}
