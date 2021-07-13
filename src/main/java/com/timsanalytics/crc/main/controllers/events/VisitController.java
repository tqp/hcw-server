package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Visit;
import com.timsanalytics.crc.main.services.events.VisitService;
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
@RequestMapping("/api/v1/visit")
@Tag(name = "Visit", description = "Visit")
public class VisitController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Visit", description = "Create Visit", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Visit> createVisit(@RequestBody Visit visit) {
        try {
            return ResponseEntity.ok()
                    .body(visitService.createVisit(visit));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Visit List", description = "Get Visit List", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Visit>> getVisitList() {
        try {
            return ResponseEntity.ok()
                    .body(this.visitService.getVisitList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Visit List (SSP)", description = "Get Visit List (SSP)", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Visit>> getVisitList_SSP(@RequestBody ServerSidePaginationRequest<Visit> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Visit> container = this.visitService.getVisitList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{visitId}", method = RequestMethod.GET)
    @Operation(summary = "Get Visit Detail", description = "Get Visit Detail", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Visit> getVisitDetail(@Parameter(description = "Visit ID", required = true) @PathVariable int visitId) {
        try {
            Visit visit = visitService.getVisitDetail(visitId);
            return ResponseEntity.ok()
                    .body(visit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Visit", description = "Update Visit", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Visit> updateVisit(@RequestBody Visit visit) {
        try {
            return ResponseEntity.ok()
                    .body(visitService.updateVisit(visit));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{visitId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Visit", description = "Delete Visit", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteVisit(@Parameter(description = "Visit GUID", required = true) @PathVariable String visitId) {
        try {
            return ResponseEntity.ok()
                    .body(visitService.deleteVisit(visitId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Visit List by Student ID", description = "Get Visit List by Student ID", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Visit>> getVisitListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.visitService.getVisitListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
