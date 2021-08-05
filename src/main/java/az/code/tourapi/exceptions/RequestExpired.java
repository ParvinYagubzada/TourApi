package az.code.tourapi.exceptions;

public class RequestExpired extends RuntimeException {
    public RequestExpired() {
        super("This request is expired.");
    }
}
