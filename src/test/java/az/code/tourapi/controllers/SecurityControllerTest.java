package az.code.tourapi.controllers;

import az.code.tourapi.exceptions.*;
import az.code.tourapi.models.dtos.*;
import az.code.tourapi.security.SecurityService;
import az.code.tourapi.security.TokenInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static az.code.tourapi.TourApiApplicationTests.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(PER_CLASS)
@ActiveProfiles("test-mvc")
@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class SecurityControllerTest {

    public static final String BASE_URL = "/api/v1/auth";
    public static final ObjectMapper mapper = new ObjectMapper();
    private static final RegisterDTO SAMPLE_REGISTER_DTO = RegisterDTO.builder()
            .agencyName(TEST_STRING).voen("1234567890")
            .username("test1234").email("test@test.com")
            .name(TEST_STRING).surname("i_am_a_tester")
            .password("test123456").build();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityService securityService;
    @MockBean
    private TokenInterceptor tokenInterceptor;

    @BeforeAll
    void setUp() throws Exception {
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("SecurityController - login() - Valid")
    void login() throws Exception {
        LoginDTO dto = LoginDTO.builder().email("test@test.com").password("12345678").build();
        LoginResponseDTO response = new LoginResponseDTO(TEST_STRING);
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
        RegisterResponseDTO response = new RegisterResponseDTO("Created");
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(SAMPLE_REGISTER_DTO);

        when(securityService.register(SAMPLE_REGISTER_DTO)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(mapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SecurityController - register() - NOT ACCEPTABLE")
    void register_AgencyNameAlreadyExists() throws Exception {
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(SAMPLE_REGISTER_DTO);
        when(securityService.register(SAMPLE_REGISTER_DTO)).thenThrow(new AgencyNameAlreadyExists(TEST_STRING));
        mockMvc
                .perform(post(BASE_URL + "/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string(TEST_STRING + " taken by another user."))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("SecurityController - register() - NOT ACCEPTABLE")
    void register_IdAlreadyTaken() throws Exception {
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(SAMPLE_REGISTER_DTO);
        when(securityService.register(SAMPLE_REGISTER_DTO)).thenThrow(new IdAlreadyTaken());
        mockMvc
                .perform(post(BASE_URL + "/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string("Username or email already taken by another user."))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("SecurityController - register() - INTERNAL SERVER ERROR")
    void register_KeycloakInternalError() throws Exception {
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(SAMPLE_REGISTER_DTO);
        when(securityService.register(SAMPLE_REGISTER_DTO)).thenThrow(new KeycloakInternalError());
        mockMvc
                .perform(post(BASE_URL + "/register")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson))
                .andExpect(content().string("Something happened when creating new user."))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("SecurityController - verify() - Valid")
    void verify() throws Exception {
        String response = "User verified.";
        when(securityService.verify(TEST_STRING, TEST_STRING)).thenReturn(response);
        mockMvc
                .perform(post(BASE_URL + "/verify")
                        .param("token", TEST_STRING)
                        .param("username", TEST_STRING))
                .andExpect(content().string(response))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("SecurityController - verify() - NOT FOUND")
    void verify_UserNotFound() throws Exception {
        when(securityService.verify(TEST_STRING, TEST_STRING)).thenThrow(new UserNotFound());
        mockMvc
                .perform(post(BASE_URL + "/verify")
                        .param("token", TEST_STRING)
                        .param("username", TEST_STRING))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("SecurityController - verify() - NOT ACCEPTABLE")
    void verify_InvalidVerificationToken() throws Exception {
        when(securityService.verify(TEST_STRING, TEST_STRING)).thenThrow(new InvalidVerificationToken());
        mockMvc
                .perform(post(BASE_URL + "/verify")
                        .param("token", TEST_STRING)
                        .param("username", TEST_STRING))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    @DisplayName("SecurityController - sendResetPasswordUrl() - Valid")
    void sendResetPasswordUrl() throws Exception {
        doNothing().when(securityService).sendResetPasswordUrl(TEST_STRING);
        mockMvc
                .perform(post(BASE_URL + "/sendResetPasswordUrl")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_STRING))
                .andExpect(status().isOk());
        Mockito.verify(securityService, times(1)).sendResetPasswordUrl(TEST_STRING);
    }

    @Test
    @DisplayName("SecurityController - sendResetPasswordUrl() - NOT FOUND")
    void sendResetPasswordUrl_UserNotFound() throws Exception {
        doThrow(new UserNotFound()).when(securityService).sendResetPasswordUrl(TEST_STRING);
        mockMvc
                .perform(post(BASE_URL + "/sendResetPasswordUrl")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_STRING))
                .andExpect(status().isNotFound());
        Mockito.verify(securityService, times(1)).sendResetPasswordUrl(TEST_STRING);
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
        UpdatePasswordDTO dto = UpdatePasswordDTO.builder()
                .oldPassword("12345678")
                .newPassword("123456789").build();
        String requestJson = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(dto);

        doNothing().when(securityService).changePassword(TEST_STRING, dto);
        mockMvc
                .perform(post(BASE_URL + "/profile/changePassword")
                        .contentType(APPLICATION_JSON_UTF8)
                        .requestAttr(ATTR_NAME, USER_DATA)
                        .content(requestJson))
                .andExpect(status().isAccepted());
        Mockito.verify(securityService, times(1)).changePassword(TEST_STRING, dto);
    }
}