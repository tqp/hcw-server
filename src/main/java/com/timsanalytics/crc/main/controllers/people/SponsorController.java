package com.timsanalytics.crc.main.controllers.people;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Sponsor;
import com.timsanalytics.crc.main.services.SponsorService;
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
@RequestMapping("/api/v1/sponsor")
@Tag(name = "Sponsor", description = "Sponsor")
public class SponsorController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final SponsorService sponsorService;

    @Autowired
    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Sponsor", tags = {"Sponsor"}, description = "Create Sponsor", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Sponsor> createSponsor(@RequestBody Sponsor sponsor) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorService.createSponsor(sponsor));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor List", tags = {"Sponsor"}, description = "Get Sponsor List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Sponsor>> getSponsorList() {
        try {
            return ResponseEntity.ok()
                    .body(this.sponsorService.getSponsorList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor List (SSP)", tags = {"Sponsor"}, description = "Get Sponsor List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Sponsor>> getSponsorList_SSP(@RequestBody ServerSidePaginationRequest<Sponsor> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Sponsor> container = this.sponsorService.getSponsorList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{sponsorId}", method = RequestMethod.GET)
    @Operation(summary = "Get Sponsor Detail", tags = {"Sponsor"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Sponsor> getSponsorDetail(@Parameter(description = "Sponsor ID", required = true) @PathVariable int sponsorId) {
        try {
            Sponsor sponsor = sponsorService.getSponsorDetail(sponsorId);
            return ResponseEntity.ok()
                    .body(sponsor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Sponsor", tags = {"Sponsor"}, description = "Update Sponsor", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Sponsor> updateSponsor(@RequestBody Sponsor sponsor) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorService.updateSponsor(sponsor));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{sponsorGuid}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Sponsor", tags = {"Sponsor"}, description = "Delete Sponsor", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteSponsor(@Parameter(description = "Sponsor GUID", required = true) @PathVariable String sponsorGuid) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorService.deleteSponsor(sponsorGuid));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // FILTERED LISTS

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor List By Student ID", description = "Get Sponsor List By Student ID", tags = {"Sponsor"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Sponsor>> getStudentListBySponsorId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.sponsorService.getSponsorListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // JOINED QUERIES

    @RequestMapping(value = "/student/{studentId}/current", method = RequestMethod.GET)
    @Operation(summary = "Get Sponsor Detail by Student ID", tags = {"Sponsor"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Sponsor> getCurrentSponsorDetailByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable int studentId) {
        try {
            Sponsor sponsor = sponsorService.getCurrentSponsorDetailByStudentId(studentId);
            return ResponseEntity.ok()
                    .body(sponsor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
