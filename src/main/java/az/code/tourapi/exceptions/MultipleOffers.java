package az.code.tourapi.exceptions;

public class MultipleOffers extends RuntimeException {
    public MultipleOffers() {
        super("You can't have more than one offer.");
    }
}
