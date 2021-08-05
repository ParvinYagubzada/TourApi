package az.code.tourapi.services;

import az.code.tourapi.models.dtos.OfferDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.FileNotFoundException;

public interface JasperService {
    File generateImage(OfferDTO dto) throws FileNotFoundException, JRException;
}
