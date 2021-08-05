package az.code.tourapi.exceptions;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("Username or password is invalid.");
    }
}
