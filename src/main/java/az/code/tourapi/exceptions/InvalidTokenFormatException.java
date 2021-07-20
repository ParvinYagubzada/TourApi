package az.code.tourapi.exceptions;

public class InvalidTokenFormatException extends RuntimeException {
    public InvalidTokenFormatException() {
        super("This token format is invalid.");
    }
}
