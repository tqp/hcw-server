package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Caregiver;
import com.timsanalytics.crc.main.services.CaregiverService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/caregiver")
@Tag(name = "Caregiver", description = "Caregiver")
public class CaregiverController {
    private final CaregiverService caregiverService;

    @Autowired
    public CaregiverController(CaregiverService caregiverService) {
        this.caregiverService = caregiverService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Caregiver", tags = {"Caregiver"}, description = "Create Caregiver", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Caregiver> createCaregiver(@RequestBody Caregiver caregiver) {
        try {
            return ResponseEntity.ok()
                    .body(caregiverService.createCaregiver(caregiver));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Caregiver List", tags = {"Caregiver"}, description = "Get Caregiver List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Caregiver>> getCaregiverList() {
        try {
            return ResponseEntity.ok()
                    .body(this.caregiverService.getCaregiverList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Caregiver List (SSP)", tags = {"Caregiver"}, description = "Get Caregiver List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Caregiver>> getCaregiverList_SSP(@RequestBody ServerSidePaginationRequest<Caregiver> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Caregiver> container = this.caregiverService.getCaregiverList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{caregiverId}", method = RequestMethod.GET)
    @Operation(summary = "Get Caregiver Detail", tags = {"Caregiver"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Caregiver> getCaregiverDetail(@Parameter(description = "Caregiver ID", required = true) @PathVariable int caregiverId) {
        try {
            Caregiver caregiver = caregiverService.getCaregiverDetail(caregiverId);
            return ResponseEntity.ok()
                    .body(caregiver);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Caregiver", tags = {"Caregiver"}, description = "Update Caregiver", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Caregiver> updateCaregiver(@RequestBody Caregiver caregiver) {
        try {
            return ResponseEntity.ok()
                    .body(caregiverService.updateCaregiver(caregiver));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{caregiverGuid}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Caregiver", tags = {"Caregiver"}, description = "Delete Caregiver", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteCaregiver(@Parameter(description = "Caregiver GUID", required = true) @PathVariable String caregiverGuid) {
        try {
            return ResponseEntity.ok()
                    .body(caregiverService.deleteCaregiver(caregiverGuid));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED QUERIES

    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    @Operation(summary = "Get Caregiver Detail by Student ID", tags = {"Caregiver"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Caregiver> getCaregiverDetailByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable int studentId) {
        try {
            Caregiver caregiver = caregiverService.getCaregiverDetailByStudentId(studentId);
            return ResponseEntity.ok()
                    .body(caregiver);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/with-loan", method = RequestMethod.GET)
    @Operation(summary = "Get Caregiver Detail by Student ID", tags = {"Caregiver"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Caregiver>> getCaregiverWithLoanList() {
        try {
            List<Caregiver> caregiverList = caregiverService.getCaregiverWithLoanList();
            return ResponseEntity.ok()
                    .body(caregiverList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
