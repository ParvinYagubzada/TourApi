package az.code.tourapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "customers")
public class CustomerInfo {
    @Id
    @Column(name = "user_id")
    String userId;
    String username;
    String phoneNumber;
    String firstName;
    String lastName;
}
