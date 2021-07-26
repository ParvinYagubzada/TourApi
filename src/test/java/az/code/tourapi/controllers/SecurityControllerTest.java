package az.code.tourapi.controllers;

import az.code.tourapi.exceptions.InvalidVerificationToken;
import az.code.tourapi.exceptions.LoginException;
import az.code.tourapi.exceptions.UserNotFound;
import az.code.tourapi.models.dtos.*;
import az.code.tourapi.security.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static az.code.tourapi.TourApiApplicationTests.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test-mvc")
@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class SecurityControllerTest {

    public static final String BASE_URL = "/api/v1/auth";
    public static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;

    @BeforeAll
    static void start() {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Test
    @DisplayName("SecurityController - login() - Valid")
    void login() throws Exception {
        LoginDTO dto = LoginDTO.builder().email("test@test.com").password("12345678").build();
        LoginResponseDTO response = new LoginResponseDTO("token");
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        when(securityService.login(dto)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SecurityController - login() - UNAUTHORIZED")
    void login_LoginException() throws Exception {
        LoginDTO dto = LoginDTO.builder().email("test@test.com").password("12345678").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        when(securityService.login(dto)).thenThrow(new LoginException());
        mockMvc
                .perform(post(BASE_URL + "/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("SecurityController - register() - Valid")
    void register() throws Exception {
        RegisterDTO dto = RegisterDTO.builder()
                .agencyName("test").voen("1234567890")
                .username("test1234").email("test@test.com")
                .name("test").surname("i_am_a_tester")
                .password("test123456").build();
        RegisterResponseDTO response = new RegisterResponseDTO("Created");
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        when(securityService.register(dto)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SecurityController - verify() - Valid")
    void verify() throws Exception {
        String token = "test", username = "test", response = "User verified.";
        when(securityService.verify(token, username)).thenReturn(response);
        mockMvc
                .perform(get(BASE_URL + "/verify")
                        .param("token", token)
                        .param("username", username))
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SecurityController - verify() - NOT FOUND")
    void verify_UserNotFount() throws Exception {
        String token = "test", username = "test";
        when(securityService.verify(token, username)).thenThrow(new UserNotFound());
        mockMvc
                .perform(get(BASE_URL + "/verify")
                        .param("token", token)
                        .param("username", username))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("SecurityController - verify() - NOT ACCEPTABLE")
    void verify_InvalidVerificationToken() throws Exception {
        String token = "test", username = "test";
        when(securityService.verify(token, username)).thenThrow(new InvalidVerificationToken());
        mockMvc
                .perform(get(BASE_URL + "/verify")
                        .param("token", token)
                        .param("username", username))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("SecurityController - sendResetPasswordUrl() - Valid")
    void sendResetPasswordUrl() throws Exception {
        String email = "test";
        doNothing().when(securityService).sendResetPasswordUrl(email);
        mockMvc
                .perform(post(BASE_URL + "/sendResetPasswordUrl")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(email))
                .andExpect(status().isOk());
        Mockito.verify(securityService, times(1)).sendResetPasswordUrl(email);
    }

    @Test
    @DisplayName("SecurityController - resetPassword() - Valid")
    void resetPassword() throws Exception {
        ResetPasswordDTO dto = ResetPasswordDTO.builder()
                .token("token")
                .username("test1234")
                .password("test123456").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        doNothing().when(securityService).resetPassword(dto);
        mockMvc
                .perform(post(BASE_URL + "/resetPassword")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());
        Mockito.verify(securityService, times(1)).resetPassword(dto);
    }

    @Test
    @DisplayName("SecurityController - changePassword() - Valid")
    void changePassword() throws Exception {
        String username = "pervinuser";
        UpdatePasswordDTO dto = UpdatePasswordDTO.builder()
                .oldPassword("12345678")
                .newPassword("123456789").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        doNothing().when(securityService).changePassword(username, dto);
        mockMvc
                .perform(post(BASE_URL + "/profile/changePassword")
                        .contentType(APPLICATION_JSON_UTF8)
                        .header(AUTHORIZATION, TOKEN)
                        .content(requestJson))
                .andExpect(status().isAccepted());
        Mockito.verify(securityService, times(1)).changePassword(username, dto);
    }
}