package az.code.tourapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("This user does not exists.");
    }
}
