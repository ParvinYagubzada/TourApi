package az.code.tourapi.models.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "verifications")
public class Verification {
    @Id
    String token;
    @OneToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private User user;
}
