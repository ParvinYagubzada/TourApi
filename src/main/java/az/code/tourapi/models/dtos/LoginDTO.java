package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginDTO {
    @Email(message = "Email must be a well-formed email address")
    @NotBlank(message = "Email must not be null or empty")
    private String email;

    @Size(min = 6, max = 15, message = "Password must be at 6-15 characters long")
    @NotBlank(message = "Password must not be null or empty")
    private String password;
}
