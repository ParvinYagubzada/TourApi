package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdatePasswordDTO {

    String oldPassword;

    @NotBlank(message = "Password must not be null or empty")
    @Size(min = 6, max = 15, message = "Password must be at 6-15 characters long")
    String newPassword;
}
