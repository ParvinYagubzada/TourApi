package az.code.tourapi.exceptions;

public class InvalidTokenFormat extends RuntimeException {
    public InvalidTokenFormat() {
        super("This token format is invalid.");
    }
}
