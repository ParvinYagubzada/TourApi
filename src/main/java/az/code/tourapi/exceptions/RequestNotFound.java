package az.code.tourapi.exceptions;

public class RequestNotFound extends RuntimeException {
    public RequestNotFound() {
        super("This request does not exists.");
    }
}
