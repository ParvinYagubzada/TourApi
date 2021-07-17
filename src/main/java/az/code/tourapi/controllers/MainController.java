package az.code.tourapi.controllers;

import az.code.tourapi.exceptions.EmailNotVerified;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class MainController {



    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> handleNotFound(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

//    @RolesAllowed("user")
//    @GetMapping("/login")
//    public ResponseEntity<?> login(@RequestAttribute("user") UserData user) {
//        return
//    }
}
