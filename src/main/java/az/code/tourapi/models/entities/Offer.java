package az.code.tourapi.models.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "offers")
@IdClass(Offer.OfferPK.class)
public class Offer {
    @Id
    private String agencyName;
    @Id
    private String uuid;

    @Type(type="text")
    private String description;
    @Type(type="text")
    private String travelLocations;
    private String travelDates;
    private Integer price;
    @Type(type="text")
    private String services;
    @Type(type="text")
    private String notes;

    @Column(name = "status")
    private Boolean isActive;
    @CreationTimestamp
    private LocalDateTime creationTime;

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OfferPK implements Serializable {
        protected String agencyName;
        protected String uuid;
    }
}
