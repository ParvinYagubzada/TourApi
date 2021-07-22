package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterDTO {
    @NotBlank(message = "Agency name must not be null or empty")
    @Size(min = 2, max = 30, message = "Agency name must be at 2-30 characters long")
    private String agencyName;

    @Pattern(regexp = "\\d{10}", message = "VOEN should contain only 10 digits")
    private String voen;

    @NotBlank(message = "Username must not be null or empty")
    @Size(min = 4, max = 20, message = "Username must be at 4-20 characters long")
    private String username;

    @NotBlank(message = "Name must not be null or empty")
    @Size(min = 2, max = 30, message = "Name must be at 2-30 characters long")
    private String name;

    @NotBlank(message = "Surname must not be null or empty")
    @Size(min = 2, max = 30, message = "Surname must be at 2-30 characters long")
    private String surname;

    @NotBlank(message = "Email must not be null or empty")
    @Email(message = "Email must be a well-formed email address")
    private String email;

    @NotBlank(message = "Password must not be null or empty")
    @Size(min = 6, max = 15, message = "Password must be at 6-15 characters long")
    private String password;
}
