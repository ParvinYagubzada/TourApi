package az.code.tourapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginException extends RuntimeException {
    public LoginException() {
        super("Username or password is invalid.");
    }
}
