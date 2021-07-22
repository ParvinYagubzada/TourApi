package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginDTO {

    @NotNull(message = "Email must not be null")
    @Email(message = "Email must be a well-formed email address")
    private String email;

    @NotNull(message = "Password must not be null")
    @Size(min = 6, max = 15, message = "Password must be at 6-15 characters long")
    private String password;
}
