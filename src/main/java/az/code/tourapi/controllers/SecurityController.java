package az.code.tourapi.controllers;

import az.code.tourapi.exceptions.EmailNotVerified;
import az.code.tourapi.exceptions.LoginException;
import az.code.tourapi.models.dtos.LoginDTO;
import az.code.tourapi.models.dtos.RegisterDTO;
import az.code.tourapi.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SecurityController {

    private final SecurityService securityService;

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleNotFound(LoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> handleNotFound(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

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
}
