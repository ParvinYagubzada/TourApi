package az.code.tourapi.models.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoginResponseDTO {
    private String token;
}
