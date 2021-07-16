package az.code.tourapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidVerificationToken extends RuntimeException {
    public InvalidVerificationToken() {
        super("This does not belong to this user or token does not exists.");
    }
}