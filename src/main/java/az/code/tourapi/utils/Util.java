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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static az.code.tourapi.models.dtos.OfferDTO.*;

public class Util {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

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

    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, timeFormatter);
    }

    public static boolean checkTime(String startTimeString, String endTimeString, LocalTime now) {
        return now.isAfter(parseTime(endTimeString)) || now.isBefore(parseTime(startTimeString));
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

    public static LocalDateTime calculateExpirationTime(LocalTime start, LocalTime end, LocalTime now, Integer deadlineHours) {
        Duration deadline = Duration.ofHours(deadlineHours);
        Duration workingHours = Duration.between(start, end);
        Duration leftHours = Duration.between(now, end);
        long days = leftHours.isNegative() || deadline.compareTo(leftHours) > 0 ? 1 : 0;
        LocalTime time = leftHours.compareTo(deadline) >= 0 && leftHours.compareTo(workingHours) < 0 ? now : start;
        deadline = deadline.minus(
                leftHours.isNegative() || leftHours.compareTo(deadline) >= 0 ? Duration.ZERO :
                        leftHours.compareTo(workingHours) > 0 && deadline.compareTo(workingHours) > 0 ?
                                workingHours : leftHours
        );
        days += deadline.dividedBy(workingHours);
        long leftMillis = deadline.toMillis() % workingHours.toMillis();
        leftMillis = leftMillis == 0 ? workingHours.toMillis() - TimeUnit.DAYS.toMillis(1) : leftMillis;
        return LocalDate.now().plusDays(days).atTime(time).plus(leftMillis, ChronoUnit.MILLIS);
    }
}
