package az.code.tourapi.exceptions;

public class EmailNotVerified extends RuntimeException {
    public EmailNotVerified() {
        super("This user's email not verified.");
    }
}
