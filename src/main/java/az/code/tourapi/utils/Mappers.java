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

import static az.code.tourapi.utils.Util.*;

@Setter
@Component
@RequiredArgsConstructor
public class Mappers {

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
        return mapper.map(dto, Request.class)
                .setActive(true)
                .setTravelStartDate(LocalDate.parse(dto.getTravelStartDate(), dateFormatter))
                .setTravelEndDate(LocalDate.parse(dto.getTravelEndDate(), dateFormatter))
                .setExpirationTime(calculateExpirationTime(
                        parseTime(startTimeString), parseTime(endTimeString), now, deadlineHours
                ));
    }
}
