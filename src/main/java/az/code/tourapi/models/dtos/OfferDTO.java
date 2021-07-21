package az.code.tourapi.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OfferDTO {
    @NotNull
    @Size(min = 10, max = 1000, message = "Description length must be min 10 and max 1000 characters long.")
    private String description;

    @NotNull
    @Pattern(regexp = "[1-3]\\d\\.[1-3]\\d\\.[1-2]\\d{3}")
    private String travelDates;

    @NotNull
    @Digits(integer = 9, fraction = 0, message = "Price can contain max 9 digits.")
    private Integer price;

    @NotNull
    @Size(min = 10, max = 1000, message = "Notes length must be min 10 and max 1000 characters long.")
    private String notes;
}
