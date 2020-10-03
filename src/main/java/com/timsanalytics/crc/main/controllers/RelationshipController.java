package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.main.beans.Relationship;
import com.timsanalytics.crc.main.services.RelationshipService;
import com.timsanalytics.crc.utils.PrintObjectService;
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

    @ResponseBody
    @RequestMapping(value = "/caregiver", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Caregiver Relationship", tags = {"Relationship"}, description = "Create Relationship", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> createCaregiverRelationship(@RequestBody Relationship relationship, @RequestHeader (name="Authorization") String token) {
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
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Relationship List By Student ID", tags = {"Relationship"}, description = "Relationship List By Student ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relationship>> getRelationshipListByStudentId(@Parameter(description = "Student GUID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipService.getRelationshipListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/relation/{relationId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Relationship List By Relation ID", tags = {"Relationship"}, description = "Relationship List By Relation ID", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relationship>> getRelationshipListByRelationId(@Parameter(description = "Relation GUID", required = true) @PathVariable Integer relationId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipService.getRelationshipListByRelationId(relationId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // OTHER

    @ResponseBody
    @RequestMapping(value = "/person", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Relationship Person", tags = {"Relationship"}, description = "Create Relationship Person", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relationship> createRelationshipPerson(@RequestBody Relationship relationship) {
        try {
            return ResponseEntity.ok()
                    .body(relationshipService.createRelationshipPerson(relationship));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
