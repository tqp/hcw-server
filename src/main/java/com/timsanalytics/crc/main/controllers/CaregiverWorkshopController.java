package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaregiverWorkshop;
import com.timsanalytics.crc.main.services.CaregiverWorkshopService;
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
@RequestMapping("/api/v1/caregiver-workshop")
@Tag(name = "Caregiver-Workshop", description = "Caregiver-Workshop")
public class CaregiverWorkshopController {
    private final CaregiverWorkshopService caregiverWorkshopService;
    private final TokenService tokenService;

    @Autowired
    public CaregiverWorkshopController(CaregiverWorkshopService caregiverWorkshopService, TokenService tokenService) {
        this.caregiverWorkshopService = caregiverWorkshopService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Caregiver-Workshop Item", description = "Create Caregiver-Workshop Item", tags = {"Caregiver-Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaregiverWorkshop> createCaregiverWorkshop(@RequestBody CaregiverWorkshop caregiverWorkshop, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(caregiverWorkshopService.createCaregiverWorkshop(username, caregiverWorkshop));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/{caregiverWorkshopId}", method = RequestMethod.GET)
    @Operation(summary = "Get Caregiver-Workshop Detail", description = "Get Caregiver-Workshop Detail", tags = {"Caregiver-Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaregiverWorkshop> getCaregiverWorkshopDetail(@Parameter(description = "Caregiver-Workshop ID", required = true) @PathVariable int caregiverWorkshopId) {
        try {
            CaregiverWorkshop caregiverWorkshop = caregiverWorkshopService.getCaregiverWorkshopDetail(caregiverWorkshopId);
            return ResponseEntity.ok()
                    .body(caregiverWorkshop);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Caregiver-Workshop", description = "Update Caregiver-Workshop", tags = {"Caregiver-Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaregiverWorkshop> updateCaregiverWorkshop(@RequestBody CaregiverWorkshop caregiverWorkshop) {
        try {
            return ResponseEntity.ok()
                    .body(caregiverWorkshopService.updateCaregiverWorkshop(caregiverWorkshop));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{caregiverWorkshopId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Caregiver-Workshop", description = "Delete Caregiver-Workshop", tags = {"Caregiver-Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValueInteger> deleteCaregiverWorkshop(@Parameter(description = "Caregiver-Workshop ID", required = true) @PathVariable int caregiverWorkshopId) {
        try {
            return ResponseEntity.ok()
                    .body(caregiverWorkshopService.deleteCaregiverWorkshop(caregiverWorkshopId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/caregiver/{caregiverId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Caregiver-Workshop List by Caregiver ID", description = "Get Caregiver-Workshop List by Caregiver ID", tags = {"Caregiver-Workshop"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CaregiverWorkshop>> getWorkshopListByCaregiverId(@Parameter(description = "Caregiver ID", required = true) @PathVariable int caregiverId) {
        try {
            return ResponseEntity.ok()
                    .body(this.caregiverWorkshopService.getWorkshopListByCaregiverId(caregiverId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
