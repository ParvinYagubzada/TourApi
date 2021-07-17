package az.code.tourapi.utils;

import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.entities.Request;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.rabbit.RawRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static az.code.tourapi.utils.Util.formatter;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MappersTest {

    @Autowired
    Mappers mappers;

    @Test
    void registerToUser() {
        RegisterDTO dto = RegisterDTO.builder()
                .companyName("test").voen(1234567890).username("test1234")
                .name("test").surname("i_am_a_tester").email("test@test.com")
                .password("test123456").build();
        User expected = User.builder()
                .username("test1234").email("test@test.com")
                .name("test i_am_a_tester").build();
        assertEquals(expected, mappers.registerToUser(dto));
    }

    @Test
    void rawToRequest() {
        LocalDate start = LocalDate.parse("12.12.1212", formatter);
        LocalDate end = LocalDate.parse("12.12.1213", formatter);
        RawRequest rawRequest = RawRequest.builder()
                .uuid("test").language("test").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate("12.12.1212").travelEndDate("12.12.1213")
                .travellerCount("1 man 2 men").budget("123")
                .build();
        Request expected = Request.builder()
                .uuid("test").language("test").tourType("test")
                .addressTo("test").addressFrom("test")
                .travelStartDate(start).travelEndDate(end)
                .travellerCount("1 man 2 men").budget(123)
                .isActive(true)
                .build();
        assertEquals(expected, mappers.rawToRequest(rawRequest));
    }
}