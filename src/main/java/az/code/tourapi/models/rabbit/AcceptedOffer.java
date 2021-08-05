package az.code.tourapi.models.rabbit;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcceptedOffer {
    String uuid;
    String agencyName;
    String username;
    String phoneNumber;
    String firstName;
    String lastName;
    String userId;
}
