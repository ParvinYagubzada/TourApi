package az.code.tourapi.exceptions;

public class OutOfWorkingHours extends RuntimeException {
    public OutOfWorkingHours() {
        super("You can't make an offer out of working hours.");
    }
}
