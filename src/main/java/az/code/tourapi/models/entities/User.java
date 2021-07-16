package az.code.tourapi.models.entities;

import az.code.tourapi.models.dtos.RegisterDTO;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {

    @Id
    private String userName;
    private String email;
    private String name;
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

    public User(RegisterDTO data) {
        this.userName = data.getUserName();
        this.email = data.getEmail();
        this.name = data.getName() + " " + data.getSurname();
        this.registerTime = LocalDateTime.now();
    }
}