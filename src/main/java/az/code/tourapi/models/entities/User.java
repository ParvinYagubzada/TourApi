package az.code.tourapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "agency_name", columnNames = "agency_name"))
public class User {

    @Id
    private String username;
    @Column(name = "agency_name")
    private String agencyName;
    private String voen;
    private String email;
    private String name;
    @CreationTimestamp
    private LocalDateTime registerTime;

    public User(String email) {
        this.email = email;
    }
}