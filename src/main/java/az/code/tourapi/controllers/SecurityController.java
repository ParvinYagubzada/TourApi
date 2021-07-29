package az.code.tourapi.controllers;

import az.code.tourapi.models.UserData;
import az.code.tourapi.models.dtos.LoginDTO;
import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.models.dtos.ResetPasswordDTO;
import az.code.tourapi.models.dtos.UpdatePasswordDTO;
import az.code.tourapi.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Profile({"!test", "mvc-test"})
@RequestMapping("/api/v1/auth")
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(securityService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(securityService.register(registerDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token, @RequestParam String username) {
        return ResponseEntity.ok(securityService.verify(token, username));
    }

    @PostMapping("/sendResetPasswordUrl")
    public ResponseEntity<?> sendResetPasswordUrl(@RequestBody String email) {
        securityService.sendResetPasswordUrl(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        securityService.resetPassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/profile/changePassword")
    public ResponseEntity<HttpStatus> changePassword(
            @RequestAttribute("user") UserData user,
            @Valid @RequestBody UpdatePasswordDTO dto
    ) {
        securityService.changePassword(user.getUsername(), dto);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
