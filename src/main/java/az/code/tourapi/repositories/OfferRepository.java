package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Offer.OfferPK> {
}
