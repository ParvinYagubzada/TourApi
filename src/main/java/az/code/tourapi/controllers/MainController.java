package az.code.tourapi.controllers;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.exceptions.EmailNotVerified;
import az.code.tourapi.models.UserData;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class MainController {

    private final ProfileService service;

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> handleNotFound(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @RolesAllowed("user")
    @GetMapping("/requests")
    public ResponseEntity<List<UserRequest>> getRequests(
            @RequestAttribute("user") UserData user,
            @RequestParam(required = false) UserRequestStatus status,
            @RequestParam(required = false, defaultValue = "false") Boolean isArchived,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "status") String sortBy
    ) {
        return ResponseEntity.ok(service
                .getRequests(user.getAgencyName(), user.getUsername(), isArchived, status, pageNo, pageSize, sortBy));
    }

    @RolesAllowed("user")
    @GetMapping("/requests/{uuid}")
    public ResponseEntity<UserRequest> getRequest(
            @PathVariable String uuid,
            @RequestAttribute("user") UserData user
    ) {
        return ResponseEntity.ok(service.getRequest(user.getAgencyName(), user.getUsername(), uuid));
    }
}
