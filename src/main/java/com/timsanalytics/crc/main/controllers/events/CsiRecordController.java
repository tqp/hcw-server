package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.CsiRecord;
import com.timsanalytics.crc.main.services.events.CsiRecordService;
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
@RequestMapping("/api/v1/csi-record")
@Tag(name = "CSI Records", description = "CSI Records")
public class CsiRecordController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CsiRecordService csiRecordService;

    @Autowired
    public CsiRecordController(CsiRecordService csiRecordService) {
        this.csiRecordService = csiRecordService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create CSI Record", description = "Create CSI Record", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CsiRecord> createCsi(@RequestBody CsiRecord csiRecord) {
        try {
            return ResponseEntity.ok()
                    .body(csiRecordService.createCsiRecord(csiRecord));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get CSI Record List", description = "Get CSI Record List", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CsiRecord>> getCsiList() {
        try {
            return ResponseEntity.ok()
                    .body(this.csiRecordService.getCsiRecordList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get CSI Record List (SSP)", description = "Get CSI Record List (SSP)", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<CsiRecord>> getCsiList_SSP(@RequestBody ServerSidePaginationRequest<CsiRecord> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<CsiRecord> container = this.csiRecordService.getCsiRecordList_SSP(serverSidePaginationRequest);
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
    @Operation(summary = "Get CSI Record Detail", description = "Get CSI Record Detail", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CsiRecord> getCsiDetail(@Parameter(description = "CSI ID", required = true) @PathVariable int csiRecordId) {
        try {
            CsiRecord csiRecord = csiRecordService.getCsiRecordDetail(csiRecordId);
            return ResponseEntity.ok()
                    .body(csiRecord);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update CSI Record", description = "Update CSI Record", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CsiRecord> updateCsi(@RequestBody CsiRecord csiRecord) {
        try {
            return ResponseEntity.ok()
                    .body(csiRecordService.updateCsiRecord(csiRecord));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{csiRecordGuId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete CSI Record", description = "Delete CSI Record", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteCsi(@Parameter(description = "CSI Record GUID", required = true) @PathVariable String csiRecordGuId) {
        try {
            return ResponseEntity.ok()
                    .body(csiRecordService.deleteCsiRecord(csiRecordGuId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get CSI Record List by Student ID", description = "Get CSI Record List by Student ID", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CsiRecord>> getCsiListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.csiRecordService.getCsiRecordListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/recent-scores/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Most Recent CSI Record Scores by Student ID", description = "Get Most Recent CSI Record Scores by Student ID", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CsiRecord> getMostRecentCsiScoresByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.csiRecordService.getMostRecentCsiRecordScoresByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/case-manager/{caseManagerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get CSI Record List by Case Manager ID", description = "Get CSI Record List by Case Manager ID", tags = {"CSI Records"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CsiRecord>> getCsiListByCaseManagerId(@Parameter(description = "Case Manager ID", required = true) @PathVariable Integer caseManagerId) {
        try {
            return ResponseEntity.ok()
                    .body(this.csiRecordService.getCsiRecordListByCaseManagerId(caseManagerId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
