package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.main.beans.Workshop;
import com.timsanalytics.crc.main.services.WorkshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workshop")
@Tag(name = "Workshop", description = "Workshop")
public class WorkshopController {
    private final WorkshopService workshopService;
    private final TokenService tokenService;

    @Autowired
    public WorkshopController(WorkshopService workshopService, TokenService tokenService) {
        this.workshopService = workshopService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Workshop Entry", description = "Create Workshop Entry", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Workshop> createWorkshopEntry(@RequestBody Workshop workshop, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(workshopService.createWorkshopEntry(username, workshop));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/caregiver/{caregiverId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Workshop List by Caregiver ID", description = "Get Workshop List by Caregiver ID", tags = {"Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Workshop>> getWorkshopListByCaregiverId(@Parameter(description = "Caregiver ID", required = true) @PathVariable int caregiverId) {
        try {
            return ResponseEntity.ok()
                    .body(this.workshopService.getWorkshopListByCaregiverId(caregiverId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
