package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginDTO {
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String password;
}
