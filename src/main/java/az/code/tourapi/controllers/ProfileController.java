package az.code.tourapi.controllers;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.UserData;
import az.code.tourapi.models.dtos.OfferDTO;
import az.code.tourapi.models.entities.UserRequest;
import az.code.tourapi.services.ProfileService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService service;

    @GetMapping("/requests")
    public ResponseEntity<List<UserRequest>> getRequests(
            @RequestAttribute("user") UserData user,
            @RequestParam(required = false) Boolean isArchived,
            @RequestParam(required = false) UserRequestStatus status,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "status") String sortBy
    ) {
        return ResponseEntity.ok(service
                .getRequests(user.getAgencyName(), isArchived, status, pageNo, pageSize, sortBy));
    }

    @GetMapping("/requests/{uuid}")
    public ResponseEntity<UserRequest> getRequest(
            @PathVariable String uuid,
            @RequestAttribute("user") UserData user
    ) {
        return ResponseEntity.ok(service.getRequest(user.getAgencyName(), uuid));
    }

    @PostMapping("/archive/{uuid}")
    public ResponseEntity<UserRequest> archiveRequest(
            @PathVariable String uuid,
            @RequestAttribute("user") UserData user
    ) {
        return new ResponseEntity<>(service.archiveRequest(user.getAgencyName(), uuid),
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/unarchive/{uuid}")
    public ResponseEntity<UserRequest> unarchiveRequest(
            @PathVariable String uuid,
            @RequestAttribute("user") UserData user
    ) {
        return new ResponseEntity<>(service.unarchiveRequest(user.getAgencyName(), uuid),
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/delete/{uuid}")
    public ResponseEntity<UserRequest> deleteRequest(
            @PathVariable String uuid,
            @RequestAttribute("user") UserData user
    ) {
        return new ResponseEntity<>(service.deleteRequest(user.getAgencyName(), uuid),
                HttpStatus.ACCEPTED);
    }

    @PostMapping("/makeOffer/{uuid}")
    public ResponseEntity<UserRequest> createOffer(
            @PathVariable String uuid,
            @Valid @RequestBody OfferDTO dto,
            @RequestAttribute("user") UserData user
    ) throws IOException, JRException {
        return new ResponseEntity<>(service.makeOffer(user.getAgencyName(), uuid, dto),
                HttpStatus.CREATED);
    }
}
