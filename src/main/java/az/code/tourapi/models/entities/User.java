package az.code.tourapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String email;
    private String name;
    @CreationTimestamp
    private LocalDateTime registerTime;

//    @OneToMany
//    @JoinColumn(name = "user_username", referencedColumnName = "username")
//    private List<Transaction> transactions;
//    @OneToMany
//    @JoinColumn(name = "user_username", referencedColumnName = "username")
//    private List<Listing> listing;
//    @OneToMany
//    @JoinColumn(name = "user_username", referencedColumnName = "username")
//    private List<Subscription> subscriptions;
//
//    public User(UserData data) {
//        this.username = data.getUsername();
//        this.name = data.getName();
//        this.phoneNumber = data.getPhoneNumber();
//        this.email = data.getEmail();
//        this.registerTime = data.getRegistrationTime();
//        this.balance = 0;
//    }
}