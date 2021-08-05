package az.code.tourapi.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OfferDTO {

    public static final String DESCRIPTION = "Description: ";
    public static final String TRAVEL_DATES = "Travel Dates: ";
    public static final String PRICE = "Price: ";
    public static final String NOTES = "Notes: ";

    @NotBlank(message = "Description must not be null or empty")
    @Size(min = 10, max = 1000, message = "Description length must be min 10 and max 1000 characters long")
    private String description;

    @NotNull(message = "Travel dates must not be null")
    @Pattern(regexp = "([0-3]\\d\\.((0[1-9])|(1[0-2]))\\.2\\d{3})-([0-3]\\d\\.((0[1-9])|(1[0-2]))\\.2\\d{3})",
            message = "Travel dates must be like XX.XX.XXXX-XX.XX.XXXX format.")
    private String travelDates;

    @NotNull(message = "Price must not be null")
    @Digits(integer = 9, fraction = 0, message = "Price can contain max 9 digits")
    private Integer price;

    @NotBlank(message = "Notes must not be null or empty")
    @Size(min = 10, max = 1000, message = "Notes length must be min 10 and max 1000 characters long")
    private String notes;
}
