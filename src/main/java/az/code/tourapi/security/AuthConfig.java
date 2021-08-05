package az.code.tourapi.security;

import az.code.tourapi.repositories.UserRepository;
import az.code.tourapi.repositories.VerificationRepository;
import az.code.tourapi.utils.MailUtil;
import az.code.tourapi.utils.Mappers;
import az.code.tourapi.utils.representations.MailRepresentation;
import az.code.tourapi.utils.representations.SimpleUserRepresentation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Setter
@Getter
@RequiredArgsConstructor
@Configuration
@Profile("!no-authConfig")
@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private final String role = "app-user";
    private final MailUtil mail;
    private final VerificationRepository verfRepo;
    private final UserRepository userRepo;
    private final Mappers mappers;
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;
    private Map<String, MailRepresentation> mails;
    private Map<String, SimpleUserRepresentation> users;
}
