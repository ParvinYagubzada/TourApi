package az.code.tourapi.security;

import az.code.tourapi.models.dtos.LoginDTO;
import az.code.tourapi.models.dtos.LoginResponseDTO;
import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.dtos.RegisterResponseDTO;

public interface SecurityService {

    LoginResponseDTO login(LoginDTO user);

    RegisterResponseDTO register(RegisterDTO register);

    String verify(String token, String username);
}
