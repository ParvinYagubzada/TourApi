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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static az.code.tourapi.utils.Util.formatter;

@Setter
@Component
@RequiredArgsConstructor
public class Mappers {

    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ModelMapper mapper;
    @Value("${app.start-time}")
    private String beginTimeString;
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
        request.setIsActive(true);
        request.setTravelStartDate(LocalDate.parse(dto.getTravelStartDate(), formatter));
        request.setTravelEndDate(LocalDate.parse(dto.getTravelEndDate(), formatter));
        LocalTime begin = LocalTime.parse(beginTimeString, timeFormatter);
        LocalTime end = LocalTime.parse(endTimeString, timeFormatter);
        if (now.isAfter(begin) && now.isBefore(end)) {
            request.setExpirationTime(LocalDate.now().atTime(now).plusHours(deadlineHours));
        } else if (now.isAfter(end)) {
            request.setExpirationTime(begin.atDate(LocalDate.now().plusDays(1)).plusHours(deadlineHours));
        } else if (now.isBefore(begin)) {
            request.setExpirationTime(begin.atDate(LocalDate.now()).plusHours(deadlineHours));
        }
        return request;
    }
}
