package az.code.tourapi.models.rabbit;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RawOffer {
    String uuid;
    String agencyName;
    byte[] data;
}
