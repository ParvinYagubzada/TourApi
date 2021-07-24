package az.code.tourapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String username;
    private String agencyName;
    private String fullName;
    private String email;
    private LocalDateTime registrationTime;
}
