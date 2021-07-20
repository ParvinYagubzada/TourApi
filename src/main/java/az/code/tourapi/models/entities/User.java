package az.code.tourapi.models.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;
    private String agencyName;
    private Integer voen;
    private String email;
    private String name;
    @CreationTimestamp
    private LocalDateTime registerTime;

    public User(String email) {
        this.email = email;
    }
}