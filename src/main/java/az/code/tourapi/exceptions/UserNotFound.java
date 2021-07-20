package az.code.tourapi.exceptions;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("This user does not exists.");
    }
}
