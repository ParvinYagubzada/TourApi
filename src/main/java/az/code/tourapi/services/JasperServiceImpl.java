package az.code.tourapi.services;

import az.code.tourapi.models.dtos.OfferDTO;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
public class JasperServiceImpl implements JasperService {

    @Override
    public File generateImage(OfferDTO dto) throws FileNotFoundException, JRException {
        File file = ResourceUtils.getFile("src/main/resources/templates/offer.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<OfferDTO> offers = new ArrayList<>();
        offers.add(dto);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(offers);
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        try{
            File out = new File("images/%s.jpg".formatted(UUID.randomUUID()));
            BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0,1f);
            ImageIO.write(image, "jpg", out);
            return out;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
