package az.code.tourapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
@Entity
@Table(name = "customers")
public class CustomerInfo {
    @Id
    @Column(name = "user_id")
    private String userId;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
}
