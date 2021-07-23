package az.code.tourapi.utils;

import az.code.tourapi.exceptions.EmailNotVerified;
import az.code.tourapi.exceptions.InvalidTokenFormat;
import az.code.tourapi.models.UserData;
import az.code.tourapi.models.dtos.OfferDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static az.code.tourapi.models.dtos.OfferDTO.*;

public class Util {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static Pageable preparePage(Integer pageNo, Integer pageSize, String sortBy) {
        return PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    }

    public static <T> List<T> getResult(Page<T> pageResult) {
        if (pageResult.hasContent()) {
            return pageResult.getContent();
        } else {
            return new LinkedList<>();
        }
    }

    @SneakyThrows
    public static File createImage(OfferDTO dto) {
        BufferedImage image = ImageIO.read(new File("images/base.jpg"));
        Font font = new Font("Arial", Font.BOLD, 38);
        Graphics g = image.getGraphics();
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(DESCRIPTION + dto.getDescription(), 50, 100);
        g.drawString(TRAVEL_DATES + dto.getTravelDates(), 50, 300);
        g.drawString(PRICE + dto.getPrice().toString(), 50, 500);
        g.drawString(NOTES + dto.getNotes(), 50, 800);
        File outputFile = new File("images/%s.jpg".formatted(UUID.randomUUID()));
        ImageIO.write(image, "jpg", outputFile);
        return outputFile;
    }

    public static UserData convertToken(String auth) {
        UserData user = new UserData();
        try {
            String[] chunks = auth.split("\\.");
            Base64.Decoder decoder = Base64.getDecoder();
            String data = new String(decoder.decode(chunks[1]));
            JsonNode payload = new ObjectMapper().readValue(data, JsonNode.class);
            if (!payload.get("email_verified").booleanValue())
                throw new EmailNotVerified();
            user.setAgencyName(payload.get("agency_name").textValue());
            user.setUsername(payload.get("preferred_username").textValue());
            user.setFullName(payload.get("name").textValue());
            user.setEmail(payload.get("email").textValue());
            user.setRegistrationTime(
                    LocalDateTime.ofEpochSecond(payload.get("creation_time").longValue(),
                            0,
                            ZoneOffset.ofHours(4)));
            return user;
        } catch (IndexOutOfBoundsException | NullPointerException | JsonProcessingException e) {
            throw new InvalidTokenFormat();
        }
    }
}
