package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Csi;
import com.timsanalytics.crc.main.services.CsiService;
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
@RequestMapping("/api/v1/csi")
@Tag(name = "CSI", description = "CSI")
public class CsiController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CsiService csiService;

    @Autowired
    public CsiController(CsiService csiService) {
        this.csiService = csiService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Csi", tags = {"CSI"}, description = "Create Csi", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Csi> createCsi(@RequestBody Csi csi) {
        try {
            return ResponseEntity.ok()
                    .body(csiService.createCsi(csi));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Csi List", description = "Get Csi List", tags = {"CSI"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Csi>> getCsiList() {
        try {
            return ResponseEntity.ok()
                    .body(this.csiService.getCsiList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Csi List (SSP)", description = "Get Csi List (SSP)", tags = {"CSI"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Csi>> getCsiList_SSP(@RequestBody ServerSidePaginationRequest<Csi> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Csi> container = this.csiService.getCsiList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{csiRecordId}", method = RequestMethod.GET)
    @Operation(summary = "Get Csi Detail", description = "Get Csi Detail", tags = {"CSI"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Csi> getCsiDetail(@Parameter(description = "Csi ID", required = true) @PathVariable int csiRecordId) {
        try {
            Csi csi = csiService.getCsiDetail(csiRecordId);
            return ResponseEntity.ok()
                    .body(csi);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Csi", description = "Update Csi", tags = {"CSI"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Csi> updateCsi(@RequestBody Csi csi) {
        try {
            return ResponseEntity.ok()
                    .body(csiService.updateCsi(csi));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{csiRecordId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Csi", description = "Delete Csi", tags = {"CSI"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteCsi(@Parameter(description = "Csi GUID", required = true) @PathVariable String csiRecordId) {
        try {
            return ResponseEntity.ok()
                    .body(csiService.deleteCsi(csiRecordId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Csi List by Student ID", description = "Get Csi List by Student ID", tags = {"Csi"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Csi>> getCsiListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.csiService.getCsiListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/case-manager/{caseManagerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Csi List by Case Manager ID", description = "Get Csi List by Case Manager ID", tags = {"Csi"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Csi>> getCsiListByCaseManagerId(@Parameter(description = "Case Manager ID", required = true) @PathVariable Integer caseManagerId) {
        try {
            return ResponseEntity.ok()
                    .body(this.csiService.getCsiListByCaseManagerId(caseManagerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
