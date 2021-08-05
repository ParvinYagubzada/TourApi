package az.code.tourapi.exceptions;

public class InvalidUnarchive extends RuntimeException {
    public InvalidUnarchive() {
        super("You can't unarchive EXPIRED or ACCEPTED requests.");
    }
}
