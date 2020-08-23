package com.timsanalytics.hcw.main.controllers;

import com.timsanalytics.hcw.common.beans.KeyValue;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.hcw.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.hcw.main.beans.CaseManager;
import com.timsanalytics.hcw.main.services.CaseManagerService;
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

@RestController
@RequestMapping("/api/v1/caseManager")
@Tag(name = "CaseManager", description = "CaseManager")
public class CaseManagerController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CaseManagerService caseManagerService;

    @Autowired
    public CaseManagerController(CaseManagerService caseManagerService) {
        this.caseManagerService = caseManagerService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create CaseManager", tags = {"CaseManager"}, description = "Create CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
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
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get CaseManager List (SSP)", tags = {"CaseManager"}, description = "Get CaseManager List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
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


    @RequestMapping(value = "/{caseManagerGuid}", method = RequestMethod.GET)
    @Operation(summary = "Get CaseManager Detail", tags = {"CaseManager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CaseManager> getCaseManagerDetail(@Parameter(description = "CaseManager GUID", required = true) @PathVariable String caseManagerGuid) {
        try {
            CaseManager caseManager = caseManagerService.getCaseManagerDetail(caseManagerGuid);
            return ResponseEntity.ok()
                    .body(caseManager);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update CaseManager", tags = {"CaseManager"}, description = "Update CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Delete CaseManager", tags = {"CaseManager"}, description = "Delete CaseManager", security = @SecurityRequirement(name = "bearerAuth"))
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
