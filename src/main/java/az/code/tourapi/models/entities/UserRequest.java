package az.code.tourapi.models.entities;

import az.code.tourapi.enums.UserRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Accessors(chain = true)
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

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id", referencedColumnName = "username")
    private CustomerInfo customer;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumns({
            @JoinColumn(name = "agency_name", referencedColumnName = "agency_name"),
            @JoinColumn(name = "uuid", referencedColumnName = "uuid")
    })
    private Offer offer;

    public UserRequest setOffer(Offer offer) {
        this.offer = offer;
        this.status = UserRequestStatus.OFFER_MADE;
        return this;
    }

    public UserRequest setCustomer(CustomerInfo customer) {
        this.customer = customer;
        this.status = UserRequestStatus.ACCEPTED;
        return this;
    }
}