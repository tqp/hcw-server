package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Caregiver;
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

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create CaseManager", tags = {"Case Manager"}, description = "Create CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Get Case Manager List", tags = {"Case Manager"}, description = "Get Case Manager List", security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Get CaseManager List (SSP)", tags = {"Case Manager"}, description = "Get CaseManager List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
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


    @RequestMapping(value = "/{caseManagerId}", method = RequestMethod.GET)
    @Operation(summary = "Get CaseManager Detail", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> getCaseManagerDetail(@Parameter(description = "CaseManager GUID", required = true) @PathVariable int caseManagerId) {
        try {
            CaseManager caseManager = caseManagerService.getCaseManagerDetail(caseManagerId);
            return ResponseEntity.ok()
                    .body(caseManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    @Operation(summary = "Get Case Manager Detail by Student ID", tags = {"Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> getCaregiverDetailByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable int studentId) {
        try {
            CaseManager caseManager = caseManagerService.getCaseManagerDetailByStudentId(studentId);
            return ResponseEntity.ok()
                    .body(caseManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update CaseManager", tags = {"Case Manager"}, description = "Update CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Delete CaseManager", tags = {"Case Manager"}, description = "Delete CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteCaseManager(@Parameter(description = "CaseManager GUID", required = true) @PathVariable String caseManagerGuid) {
        try {
            return ResponseEntity.ok()
                    .body(caseManagerService.deleteCaseManager(caseManagerGuid));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
