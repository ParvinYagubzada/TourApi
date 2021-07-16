package az.code.tourapi.models.dtos;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegisterDTO {
    @NotNull
    @Size(min = 2, max = 30, message = "Company name must be at 2-30 characters long")
    private String companyName;

    @Digits(integer = 10, fraction = 0)
    private Integer voen;

    @NotNull
    @Size(min = 4, max = 20, message = "Username must be at 4-20 characters long")
    private String userName;

    @NotNull
    @Size(min = 2, max = 30, message = "Name must be at 2-30 characters long")
    private String name;

    @NotNull
    @Size(min = 2, max = 30, message = "Surname must be at 2-30 characters long")
    private String surname;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 15, message = "Password must be at 6-15 characters long")
    private String password;
}
