package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.KeyValueLong;
import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.Relationship;
import com.timsanalytics.crc.main.services.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relationship")
@Tag(name = "Relationship", description = "Relationship")
public class RelationshipController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final RelationshipService relationshipService;
    private final TokenService tokenService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, TokenService tokenService) {
        this.relationshipService = relationshipService;
        this.tokenService = tokenService;
    }

    // CAREGIVER

    @ResponseBody
    @RequestMapping(value = "/caregiver", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Caregiver Relationship", description = "Create Caregiver Relationship", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> createCaregiverRelationship(@RequestBody Relationship relationship, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.createCaregiverRelationship(username, relationship));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/caregiver/{caregiverId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List By Caregiver ID", description = "Get Student List By Caregiver ID", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relationship>> getStudentListByCaregiverId(@Parameter(description = "Caregiver ID", required = true) @PathVariable Integer caregiverId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipService.getStudentListByCaregiverId(caregiverId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/caregiver", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Caregiver Relationship", description = "Update Caregiver Relationship", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> updateCaregiverRelationship(@RequestBody Relationship relationship, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.updateCaregiverRelationship(username, relationship));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/caregiver/{relationshipId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Caregiver Relationship", description = "Delete Caregiver Relationship", tags = {"Caregiver"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValueLong> deleteCaregiver(@Parameter(description = "Relationship ID", required = true) @PathVariable Integer relationshipId) {
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.deleteCaregiverRelationship(relationshipId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // CASE MANAGER

    @ResponseBody
    @RequestMapping(value = "/case-manager", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Case Manager Relationship", description = "Create Case Manager Relationship", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> createCaseManagerRelationship(@RequestBody Relationship relationship, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.createCaseManagerRelationship(username, relationship));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/case-manager/{caseManagerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List By Case Manager ID", description = "Get Student List By Case Manager ID", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relationship>> getStudentListByCaseManagerId(@Parameter(description = "Case Manager ID", required = true) @PathVariable Integer caseManagerId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipService.getStudentListByCaseManagerId(caseManagerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // SPONSOR

    @ResponseBody
    @RequestMapping(value = "/sponsor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Sponsor Relationship", description = "Create Sponsor Relationship", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> createSponsorRelationship(@RequestBody Relationship relationship, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.createSponsorRelationship(username, relationship));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sponsor/{sponsorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List By Sponsor ID", description = "Get Student List By Sponsor ID", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relationship>> getStudentListBySponsorId(@Parameter(description = "Sponsor ID", required = true) @PathVariable Integer sponsorId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipService.getStudentListBySponsorId(sponsorId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // PROGRAM STATUS

    @ResponseBody
    @RequestMapping(value = "/program-status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Program Status Entry", description = "Create Program Status Entry", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProgramStatus> createProgramStatusEntry(@RequestBody ProgramStatus programStatus, @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.createProgramStatusEntry(username, programStatus));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
