package az.code.tourapi.exceptions;

public class AgencyNameAlreadyExists extends RuntimeException {
    public AgencyNameAlreadyExists(String agencyName) {
        super(agencyName + " taken by another user.");
    }
}
