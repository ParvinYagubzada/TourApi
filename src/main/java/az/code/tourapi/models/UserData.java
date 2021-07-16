package az.code.tourapi.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserData {
    private String userName;
    private String companyName;
    private String fullName;
    private String email;
    private LocalDateTime registrationTime;
}
