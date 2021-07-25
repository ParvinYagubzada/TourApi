package az.code.tourapi.security;

import az.code.tourapi.exceptions.InvalidVerificationToken;
import az.code.tourapi.exceptions.LoginException;
import az.code.tourapi.exceptions.UserNotFound;
import az.code.tourapi.models.dtos.*;
import az.code.tourapi.models.entities.User;
import az.code.tourapi.models.entities.Verification;
import az.code.tourapi.repositories.UserRepository;
import az.code.tourapi.repositories.VerificationRepository;
import az.code.tourapi.utils.MailUtil;
import az.code.tourapi.utils.Mappers;
import az.code.tourapi.utils.Util;
import az.code.tourapi.utils.representations.MailRepresentation;
import lombok.*;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Example;

import javax.ws.rs.core.Response;
import java.util.*;

@SuppressWarnings("DuplicatedCode")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"mail", "verfRepo", "userRepo", "mappers"})
@EqualsAndHashCode(exclude = {"mail", "verfRepo", "userRepo", "mappers"})
public class SecurityServiceImpl implements SecurityService {

    @SuppressWarnings("FieldCanBeLocal")
    private String role;

    private MailUtil mail;
    private VerificationRepository verfRepo;
    private UserRepository userRepo;
    private Mappers mappers;

    private String authServerUrl;
    private String realm;
    private String clientId;
    private String clientSecret;

    private String adminUsername;
    private String adminPassword;

    private Map<String, MailRepresentation> mails;

    @Override
    public LoginResponseDTO login(LoginDTO user) {
        Map<String, Object> clientCredentials = new HashMap<>();
        clientCredentials.put("secret", clientSecret);
        clientCredentials.put("grant_type", "password");
        Configuration configuration =
                new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
        AuthzClient authzClient = AuthzClient.create(configuration);
        try {
            AccessTokenResponse response =
                    authzClient.obtainAccessToken(user.getEmail(), user.getPassword());
            Util.convertToken(response.getToken());
            return LoginResponseDTO.builder().token(response.getToken()).build();
        } catch (HttpResponseException exception) {
            throw new LoginException();
        }
    }

    @Override
    public RegisterResponseDTO register(RegisterDTO register) {
        Keycloak keycloak = getRealmCli();
        UserRepresentation user = createUser(register);
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            UserResource userResource = resetPassword(register.getPassword(),
                    usersResource.get(CreatedResponseUtil.getCreatedId(response)));
            RoleRepresentation realmRoleUser = realmResource.roles().get(role).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(realmRoleUser));
            sendVerificationEmail(register);
        }
        keycloak.tokenManager().getAccessToken();
        return RegisterResponseDTO.builder().message(response.getStatusInfo().toString()).build();
    }

    private void sendVerificationEmail(RegisterDTO register) {
        String token = UUID.randomUUID().toString();
        userRepo.save(mappers.registerToUser(register));
        verfRepo.save(Verification.builder()
                .token(token)
                .user(User.builder().username(register.getUsername()).build()).build());
        MailRepresentation representation = mails.get("verification");
        mail.sendNotificationEmail(register.getEmail(), representation.getSubject(),
                representation.getContext().formatted(representation
                        .getUrls()
                        .get("verification-url")
                        .formatted(token, register.getUsername())
                ));
    }

    private UserResource resetPassword(String password, UserResource userResource) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        userResource.resetPassword(passwordCred);
        return userResource;
    }

    private UserRepresentation createUser(RegisterDTO register) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(register.getUsername());
        user.setFirstName(register.getName());
        user.setLastName(register.getSurname());
        user.setEmail(register.getEmail());
        user.setAttributes(Map.of(
                "agency_name", Collections.singletonList(register.getAgencyName()),
                "voen", Collections.singletonList(register.getVoen())
        ));
        return user;
    }

    @Override
    public String verify(String token, String username) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(username);
        if (users.size() == 0)
            throw new UserNotFound();
        UserRepresentation search = users.get(0);
        UserResource user = usersResource.get(search.getId());
        search.setEmailVerified(true);
        Optional<Verification> verf = verfRepo.findByTokenAndUser_Username(token, username);
        if (verf.isEmpty())
            throw new InvalidVerificationToken();
        verfRepo.delete(verf.get());
        user.update(search);
        return "User verified.";
    }

    @Override
    public void sendResetPasswordUrl(String email) {
        Example<User> example = Example.of(new User(email));
        Optional<User> user;
        if ((user = userRepo.findOne(example)).isPresent()) {
            String token = UUID.randomUUID().toString();
            verfRepo.save(Verification.builder()
                    .token(token)
                    .user(user.get()).build());
            MailRepresentation representation = mails.get("reset-password");
            mail.sendNotificationEmail(email, representation.getSubject(),
                    representation.getContext().formatted(representation
                            .getUrls()
                            .get("reset-url")
                            .formatted(token, user.get().getUsername())
                    ));
        }
    }

    @Override
    public void changePassword(String username, UpdatePasswordDTO dto) {
        login(new LoginDTO(username, dto.getOldPassword()));
        resetUserPassword(username, dto.getNewPassword());
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        Optional<Verification> verf = verfRepo.findByTokenAndUser_Username(dto.getToken(), dto.getUsername());
        if (verf.isEmpty())
            throw new InvalidVerificationToken();
        verfRepo.delete(verf.get());
        resetUserPassword(dto.getUsername(), dto.getPassword());
    }

    private void resetUserPassword(String username, String newPassword) {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> users = usersResource.search(username);
        if (users.size() == 0)
            throw new UserNotFound();
        UserRepresentation search = users.get(0);
        UserResource user = usersResource.get(search.getId());
        resetPassword(newPassword, user);
    }

    private UsersResource getUsersResource() {
        Keycloak keycloak = getRealmCli();
        RealmResource realmResource = keycloak.realm(realm);
        return realmResource.users();
    }

    private Keycloak getRealmCli() {
        return KeycloakBuilder.builder().serverUrl(authServerUrl)
                .grantType(OAuth2Constants.PASSWORD).realm("master").clientId("admin-cli")
                .username(adminUsername).password(adminPassword)
                .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build();
    }
}
