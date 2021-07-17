package az.code.tourapi.models.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "customers")
public class CustomerInfo {
    @Id
    String username;
    String phoneNumber;
    String firstName;
    String lastName;
    String userId;
}
