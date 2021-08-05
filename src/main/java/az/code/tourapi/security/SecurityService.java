package az.code.tourapi.security;

import az.code.tourapi.models.dtos.*;

public interface SecurityService {

    LoginResponseDTO login(LoginDTO user);

    RegisterResponseDTO register(RegisterDTO register);

    String verify(String token, String username);

    void sendResetPasswordUrl(String email);

    void changePassword(String username, UpdatePasswordDTO dto);

    void resetPassword(ResetPasswordDTO dto);
}
