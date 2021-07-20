package az.code.tourapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException() {
        super("This token format is invalid.");
    }
}
