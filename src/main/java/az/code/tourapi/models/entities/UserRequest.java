package az.code.tourapi.models.entities;

import az.code.tourapi.enums.UserRequestStatus;
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

    @Enumerated(EnumType.ORDINAL)
    private UserRequestStatus status;

    private boolean isArchived;

    @OneToOne(optional = false)
    @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false)
    private Request request;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "username")
    private CustomerInfo customer;

    @OneToOne
    private Offer offer;

    @Getter
    @Setter
    @Builder
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRequestPK implements Serializable {
        protected String username;
        protected String agencyName;
        protected String uuid;
    }
}