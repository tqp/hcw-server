package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.common.beans.KeyValueInteger;
import com.timsanalytics.crc.main.beans.CaseManagerQualification;
import com.timsanalytics.crc.main.services.events.CaseManagerQualificationService;
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
@RequestMapping("/api/v1/case-manager-qualification")
@Tag(name = "Case Manager Qualification", description = "Case Manager Qualification")
public class CaseManagerQualificationController {
    private final CaseManagerQualificationService caseManagerQualificationService;
    private final TokenService tokenService;

    @Autowired
    public CaseManagerQualificationController(CaseManagerQualificationService caseManagerQualificationService, TokenService tokenService) {
        this.caseManagerQualificationService = caseManagerQualificationService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Case Manager Qualification Item", description = "Create Case Manager Qualification Item", tags = {"Case Manager Qualification"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManagerQualification> createCaseManagerQualification(@RequestBody CaseManagerQualification CaseManagerQualification,
                                                                                   @RequestHeader(name = "Authorization") String token) {
        String username = this.tokenService.getUsernameFromToken(token.replaceFirst("Bearer ", ""));
        try {
            return ResponseEntity.ok()
                    .body(caseManagerQualificationService.createCaseManagerQualification(username, CaseManagerQualification));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/{CaseManagerQualificationId}", method = RequestMethod.GET)
    @Operation(summary = "Get Case Manager Qualification Detail", description = "Get Case Manager Qualification Detail", tags = {"Case Manager Qualification"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManagerQualification> getCaseManagerQualificationDetail(@Parameter(description = "Case Manager Qualification ID", required = true) @PathVariable int CaseManagerQualificationId) {
        try {
            CaseManagerQualification CaseManagerQualification = caseManagerQualificationService.getCaseManagerQualificationDetail(CaseManagerQualificationId);
            return ResponseEntity.ok()
                    .body(CaseManagerQualification);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Case Manager Qualification", description = "Update Case Manager Qualification", tags = {"Case Manager Qualification"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManagerQualification> updateCaseManagerQualification(@RequestBody CaseManagerQualification CaseManagerQualification) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerQualificationService.updateCaseManagerQualification(CaseManagerQualification));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{CaseManagerQualificationId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Case Manager Qualification", description = "Delete Case Manager Qualification", tags = {"Case Manager Qualification"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValueInteger> deleteCaseManagerQualification(@Parameter(description = "Case Manager Qualification ID", required = true) @PathVariable int CaseManagerQualificationId) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerQualificationService.deleteCaseManagerQualification(CaseManagerQualificationId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/case-manager/{caseManagerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Case Manager Qualification List by Case Manager ID", description = "Get Case Manager Qualification List by Case Manager ID", tags = {"Case Manager Qualification"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CaseManagerQualification>> getQualificationListByCaseManagerId(@Parameter(description = "Case Manager ID", required = true) @PathVariable int caseManagerId) {
        try {
            return ResponseEntity.ok()
                    .body(this.caseManagerQualificationService.getQualificationListByCaseManagerId(caseManagerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
