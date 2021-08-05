package az.code.tourapi.exceptions;

public class Unauthorized extends RuntimeException {
    public Unauthorized() {
        super("This user is not authorized.");
    }
}
