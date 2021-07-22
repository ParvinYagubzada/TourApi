package az.code.tourapi.models.entities;

import az.code.tourapi.enums.UserRequestStatus;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder(toBuilder = true)
@Entity
@Table(name = "user_requests")
public class UserRequest {

    @EmbeddedId
    RequestId id;

    @Enumerated(EnumType.ORDINAL)
    private UserRequestStatus status;

    private boolean isArchived;

    @OneToOne
    @JoinColumn(name = "uuid", referencedColumnName = "uuid", insertable = false, updatable = false, nullable = false)
    private Request request;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "username")
    private CustomerInfo customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "agency_name", referencedColumnName = "agency_name"),
            @JoinColumn(name = "uuid", referencedColumnName = "uuid")
    })
    private Offer offer;

    public void setOffer(Offer offer) {
        this.offer = offer;
        this.status = UserRequestStatus.OFFER_MADE;
    }
//
//    public void setCustomer(CustomerInfo customer) {
//        this.customer = customer;
//        this.status = UserRequestStatus.ACCEPTED;
//    }
}