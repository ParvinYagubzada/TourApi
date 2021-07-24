package az.code.tourapi.controllers;

import az.code.tourapi.models.dtos.*;
import az.code.tourapi.security.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class SecurityControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);
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
    void login() throws Exception {
        LoginDTO dto = LoginDTO.builder().email("test@test.com").password("12345678").build();
        LoginResponseDTO response = new LoginResponseDTO("token");
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        when(securityService.login(dto)).thenReturn(response);
        mockMvc
                .perform(post("/api/v1/auth/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
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
                .perform(post("/api/v1/auth/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    void verify() throws Exception {
        String token = "test", username = "test";
        String response = "User verified.";

        when(securityService.verify(token, username)).thenReturn(response);
        mockMvc
                .perform(get("/api/v1/auth/verify")
                        .param("token", token)
                        .param("username", username))
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    void sendResetPasswordUrl() throws Exception {
        String email = "test";

        doNothing().when(securityService).sendResetPasswordUrl(email);
        mockMvc
                .perform(post("/api/v1/auth/sendResetPasswordUrl")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(email))
                .andExpect(status().isOk());
        Mockito.verify(securityService, times(1))
                .sendResetPasswordUrl(email);
    }

    @Test
    void resetPassword() throws Exception {
        ResetPasswordDTO dto = ResetPasswordDTO.builder()
                .token("token")
                .username("test1234")
                .password("test123456").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        doNothing().when(securityService).resetPassword(dto);
        mockMvc
                .perform(post("/api/v1/auth/resetPassword")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(status().isOk());
        Mockito.verify(securityService, times(1)).resetPassword(dto);
    }

    @Test
    void changePassword() throws Exception {
        String username = "pervinuser";
        UpdatePasswordDTO dto = UpdatePasswordDTO.builder()
                .oldPassword("12345678")
                .newPassword("123456789").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        doNothing().when(securityService).changePassword(username, dto);
        mockMvc
                .perform(post("/api/v1/auth/profile/changePassword")
                        .contentType(APPLICATION_JSON_UTF8)
                        .header("Authorization", ".eyJleHAiOjE2MjY5OTg2MDQsImlhdCI6MTYyNjk2MjYwNCwianRpIjoiZmJlOTdmZDYtNTNlMC00MGZkLWFkMzItZDExOTIwNjI3NjFmIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL1RvdXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMmFkYTk2ZDAtNmExMC00ZThjLTg2YmQtMzQzOGE3Zjk2OWVmIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG91ci1hcHAiLCJzZXNzaW9uX3N0YXRlIjoiN2I1NGFhMjktMWE3Yy00NDFkLWIzMDItMTNiZDQyZmIzZTFjIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiYXBwLXVzZXIiLCJkZWZhdWx0LXJvbGVzLXRvdXIiXX0sInJlc291cmNlX2FjY2VzcyI6eyJ0b3VyLWFwcCI6eyJyb2xlcyI6WyJ1c2VyIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJjcmVhdGlvbl90aW1lIjoxNjI2OTYxOTQwOTI2LCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYWdlbmN5X25hbWUiOiJHbG9iYWwgVHJhdmVsIiwidm9lbiI6IjEyMzQ1Njc4OTAiLCJuYW1lIjoiUGVydmluIFlhcXViemFkZSIsInByZWZlcnJlZF91c2VybmFtZSI6InBlcnZpbnVzZXIiLCJnaXZlbl9uYW1lIjoiUGVydmluIiwiZmFtaWx5X25hbWUiOiJZYXF1YnphZGUiLCJlbWFpbCI6InBhcnZpbnl5QGNvZGUuZWR1LmF6In0.")
                        .content(requestJson))
                .andExpect(status().isAccepted());
        Mockito.verify(securityService, times(1)).changePassword(username, dto);
    }
}