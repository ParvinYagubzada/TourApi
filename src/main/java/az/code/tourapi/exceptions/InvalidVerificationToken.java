package az.code.tourapi.exceptions;

public class InvalidVerificationToken extends RuntimeException {
    public InvalidVerificationToken() {
        super("This does not belong to this user or token does not exists.");
    }
}