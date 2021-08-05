package az.code.tourapi.models.rabbit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RawRequest {
    private String uuid;
    private String language;
    private String tourType;
    private String addressTo;
    private String addressFrom;
    private String travelStartDate;
    private String travelEndDate;
    private String travellerCount;
    private String budget;
}
