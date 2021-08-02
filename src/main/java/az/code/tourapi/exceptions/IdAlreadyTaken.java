package az.code.tourapi.exceptions;

public class IdAlreadyTaken extends RuntimeException {
    public IdAlreadyTaken() {
        super("Username or email already taken by another user.");
    }
}
