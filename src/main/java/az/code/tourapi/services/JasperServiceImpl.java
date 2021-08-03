package az.code.tourapi.services;

import az.code.tourapi.models.dtos.OfferDTO;
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

@Service
public class JasperServiceImpl {

    public File generateImage(OfferDTO dto) throws FileNotFoundException, JRException {

        File file = ResourceUtils.getFile("C:\\Users\\Parvin Yaqubzada\\OneDrive\\IdeaProjects\\TourApi\\src\\main\\resources\\offer.jrxml");
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
            e.printStackTrace();
        }
        return null;
    }

}
