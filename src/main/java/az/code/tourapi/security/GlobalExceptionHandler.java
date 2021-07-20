package az.code.tourapi.security;

import az.code.tourapi.exceptions.EmailNotVerified;
import az.code.tourapi.exceptions.InvalidTokenFormatException;
import az.code.tourapi.exceptions.LoginException;
import az.code.tourapi.exceptions.Unauthorized;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<String> exceptionHandler(Unauthorized e) {
        System.out.println("1");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> exceptionHandler(LoginException e) {
        System.out.println("2");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> exceptionHandler(EmailNotVerified e) {
        System.out.println("3");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidTokenFormatException.class)
    public ResponseEntity<String> exceptionHandler(InvalidTokenFormatException e) {
        System.out.println("4");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
