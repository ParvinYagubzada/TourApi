package az.code.tourapi.exceptions;

public class KeycloakInternalError extends RuntimeException {
    public KeycloakInternalError() {
        super("Something happened when creating new user.");
    }
}
