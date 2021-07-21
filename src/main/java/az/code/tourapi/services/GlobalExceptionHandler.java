package az.code.tourapi.services;

import az.code.tourapi.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<String> exceptionHandler(Unauthorized e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> exceptionHandler(LoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> exceptionHandler(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidTokenFormat.class)
    public ResponseEntity<String> exceptionHandler(InvalidTokenFormat e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidVerificationToken.class)
    public ResponseEntity<String> exceptionHandler(InvalidVerificationToken e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> exceptionHandler(UserNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotFound.class)
    public ResponseEntity<String> exceptionHandler(RequestNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestExpired.class)
    public ResponseEntity<String> exceptionHandler(RequestExpired e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MultipleOffers.class)
    public ResponseEntity<String> exceptionHandler(MultipleOffers e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
