package az.code.tourapi.models.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "user_requests")
@IdClass(UserRequest.UserRequestPK.class)
public class UserRequest {

    @Id
    private String username;
    @Id
    private String agencyName;
    @Id
    private String uuid;

    private boolean isArchived;

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "uuid")
    private Request request;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "username")
    private CustomerInfo customer;

    @OneToOne
    private Offer offer;

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRequestPK implements Serializable {
        protected String username;
        protected String agencyName;
        protected String uuid;
    }
}