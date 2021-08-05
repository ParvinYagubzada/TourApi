package az.code.tourapi.repositories;

import az.code.tourapi.models.entities.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerInfo, String> {
}
