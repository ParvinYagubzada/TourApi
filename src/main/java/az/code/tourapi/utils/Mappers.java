package az.code.tourapi.utils;

import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.rabbit.RawRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static az.code.tourapi.utils.Util.formatter;

@Component
@RequiredArgsConstructor
public class Mappers {

    private final ModelMapper mapper;

    public User registerToUser(RegisterDTO dto) {
        User user = mapper.map(dto, User.class);
        user.setName(dto.getName() + " " + dto.getSurname());
        return user;
    }

    public Request rawToRequest(RawRequest dto) {
        Request request = mapper.map(dto, Request.class);
        request.setIsActive(true);
        request.setTravelStartDate(LocalDate.parse(dto.getTravelStartDate(), formatter));
        request.setTravelEndDate(LocalDate.parse(dto.getTravelEndDate(), formatter));
        return request;
    }
}
