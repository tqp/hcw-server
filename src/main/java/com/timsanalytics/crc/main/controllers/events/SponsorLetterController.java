package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.main.beans.PostGradEvent;
import com.timsanalytics.crc.main.beans.SponsorLetter;
import com.timsanalytics.crc.main.services.events.SponsorLetterService;
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
@RequestMapping("/api/v1/sponsor-letter")
@Tag(name = "Sponsor Letter", description = "Sponsor Letter")
public class SponsorLetterController {
    private final SponsorLetterService sponsorLetterService;

    @Autowired
    public SponsorLetterController(SponsorLetterService sponsorLetterService) {
        this.sponsorLetterService = sponsorLetterService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Sponsor Letter", description = "Create Sponsor Letter", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SponsorLetter> createSponsorLetter(@RequestBody SponsorLetter sponsorLetter) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorLetterService.createSponsorLetter(sponsorLetter));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor Letter List", description = "Get Sponsor Letter List", tags = {"Visit"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<SponsorLetter>> getSponsorLetterList() {
        try {
            return ResponseEntity.ok()
                    .body(this.sponsorLetterService.getSponsorLetterList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{sponsorLetterId}", method = RequestMethod.GET)
    @Operation(summary = "Get Sponsor Letter Detail", description = "Get Sponsor Letter Detail", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SponsorLetter> getSponsorLetterDetail(@Parameter(description = "Student Sponsor Letter ID", required = true) @PathVariable int sponsorLetterId) {
        try {
            SponsorLetter sponsorLetter = sponsorLetterService.getSponsorLetterDetail(sponsorLetterId);
            return ResponseEntity.ok()
                    .body(sponsorLetter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Sponsor Letter", description = "Update Sponsor Letter", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<SponsorLetter> updateSponsorLetter(@RequestBody SponsorLetter sponsorLetter) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorLetterService.updateSponsorLetter(sponsorLetter));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{sponsorLetterId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Sponsor Letter", description = "Delete Sponsor Letter", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteSponsorLetter(@Parameter(description = "Sponsor Letter ID", required = true) @PathVariable String sponsorLetterId) {
        try {
            return ResponseEntity.ok()
                    .body(sponsorLetterService.deleteSponsorLetter(sponsorLetterId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student Sponsor Letter List by Student ID", description = "Get Sponsor Letter List by Student ID", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<SponsorLetter>> getStudentSponsorLetterListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.sponsorLetterService.getSponsorLetterListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sponsor/{sponsorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student Sponsor Letter List by Student ID", description = "Get Sponsor Letter List by Student ID", tags = {"Sponsor Letter"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<SponsorLetter>> getStudentSponsorLetterListBySponsorId(@Parameter(description = "Sponsor ID", required = true) @PathVariable Integer sponsorId) {
        try {
            return ResponseEntity.ok()
                    .body(this.sponsorLetterService.getSponsorLetterListBySponsorId(sponsorId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
