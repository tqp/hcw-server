package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.CaseManager;
import com.timsanalytics.crc.main.services.CaseManagerService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/case-manager")
@Tag(name = "Case Manager", description = "CaseManager")
public class CaseManagerController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CaseManagerService caseManagerService;

    @Autowired
    public CaseManagerController(CaseManagerService caseManagerService) {
        this.caseManagerService = caseManagerService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Case Manager", description = "Create Case Manager", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> createCaseManager(@RequestBody CaseManager caseManager) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerService.createCaseManager(caseManager));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Case Manager List", description = "Get Case Manager List", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CaseManager>> getCaseManagerList() {
        try {
            return ResponseEntity.ok()
                    .body(this.caseManagerService.getCaseManagerList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Case Manager List (SSP)", description = "Get Case Manager List (SSP)", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<CaseManager>> getCaseManagerList_SSP(@RequestBody ServerSidePaginationRequest<CaseManager> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<CaseManager> container = this.caseManagerService.getCaseManagerList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @Operation(summary = "Get Case Manager Detail", description = "Get Case Manager Detail", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> getCaseManagerDetail(@Parameter(description = "Case Manager GUID", required = true) @PathVariable int userId) {
        try {
            CaseManager caseManager = caseManagerService.getCaseManagerDetail(userId);
            return ResponseEntity.ok()
                    .body(caseManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Case Manager", description = "Update Case Manager", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> updateCaseManager(@RequestBody CaseManager caseManager) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerService.updateCaseManager(caseManager));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{caseManagerGuid}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Case Manager", description = "Delete Case Manager", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteCaseManager(@Parameter(description = "Case Manager GUID", required = true) @PathVariable String caseManagerGuid) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerService.deleteCaseManager(caseManagerGuid));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED QUERIES

    @RequestMapping(value = "/student/{studentId}/current", method = RequestMethod.GET)
    @Operation(summary = "Get Current Case Manager Detail by Student ID", description = "Get Current Case Manager Detail by Student ID", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> getCurrentCaseManagerDetailByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable int studentId) {
        try {
            CaseManager caseManager = caseManagerService.getCurrentCaseManagerDetailByStudentId(studentId);
            return ResponseEntity.ok()
                    .body(caseManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
