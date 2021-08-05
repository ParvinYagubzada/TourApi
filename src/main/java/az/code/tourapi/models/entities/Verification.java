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
    private String token;
    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
