package com.timsanalytics.crc.main.controllers;

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

    @Autowired
    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
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
