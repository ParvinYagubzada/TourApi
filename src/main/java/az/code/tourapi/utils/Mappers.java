package az.code.tourapi.utils;

import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.entities.*;
import az.code.tourapi.models.rabbit.AcceptedOffer;
import az.code.tourapi.models.rabbit.RawRequest;
import az.code.tourapi.security.AuthConfig;
import az.code.tourapi.security.SecurityServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static az.code.tourapi.utils.Util.formatter;

@Setter
@Component
@RequiredArgsConstructor
public class Mappers {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ModelMapper mapper;
    @Value("${app.start-time}")
    private String startTimeString;
    @Value("${app.end-time}")
    private String endTimeString;
    @Value("${app.deadline}")
    private Integer deadlineHours;

    public CustomerInfo acceptedToCustomer(AcceptedOffer acceptedOffer) {
        return mapper.map(acceptedOffer, CustomerInfo.class);
    }

    public User registerToUser(RegisterDTO dto) {
        User user = mapper.map(dto, User.class);
        user.setName(dto.getName() + " " + dto.getSurname());
        return user;
    }

    public Offer dtoToOffer(OfferDTO dto, String agencyName, String uuid) {
        Offer offer = mapper.map(dto, Offer.class);
        offer.setIsActive(true);
        offer.setId(new RequestId(agencyName, uuid));
        return offer;
    }

    public SecurityServiceImpl configToService(AuthConfig config) {
        SecurityServiceImpl service = mapper.map(config, SecurityServiceImpl.class);
        service.setAdminUsername(config.getUsers().get("admin").getUsername());
        service.setAdminPassword(config.getUsers().get("admin").getPassword());
        return service;
    }

    public Request rawToRequest(RawRequest dto, LocalTime now) {
        Request request = mapper.map(dto, Request.class);
        request.setActive(true);
        request.setTravelStartDate(LocalDate.parse(dto.getTravelStartDate(), formatter));
        request.setTravelEndDate(LocalDate.parse(dto.getTravelEndDate(), formatter));
        LocalTime start = LocalTime.parse(startTimeString, timeFormatter);
        LocalTime end = LocalTime.parse(endTimeString, timeFormatter);
        Duration deadline = Duration.ofHours(deadlineHours);
        Duration workingHours = Duration.between(start, end);
        if (now.isAfter(end)) {
            LocalTime time = deadline.compareTo(workingHours) > 0 ? end : start.plusHours(deadlineHours);
            calculateExpirationTime(request, start, time, Duration.ZERO, deadline);
        } else if (now.isBefore(start)) {
            Duration time = deadline.compareTo(workingHours) > 0 ? workingHours : Duration.ZERO;
            calculateExpirationTime(request, start, end, time, deadline);
        } else if (now.isAfter(start) && now.isBefore(end)) {
            Duration leftHours = Duration.between(now, end);
            if (leftHours.compareTo(deadline) > 0) {
                request.setExpirationTime(LocalDate.now().atTime(now).plusHours(deadlineHours));
            } else {
                calculateExpirationTime(request, start, end, leftHours, deadline);
            }
        }
        return request;
    }

    private void calculateExpirationTime(Request request, LocalTime start, LocalTime end, Duration leftHours, Duration deadline) {
        Duration workingHours = Duration.between(start, end);
        deadline = deadline.minus(leftHours);
        long times = deadline.dividedBy(workingHours);
        long left = deadline.toMillis() % workingHours.toMillis();
        times = left != 0 && times != 0 ? times + 1 : times;
        LocalTime time = left != 0 ? start : end;
        request.setExpirationTime(LocalDate.now().plusDays(times).atTime(time).plus(left, ChronoUnit.MILLIS));
    }
}
