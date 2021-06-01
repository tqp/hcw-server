package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.main.beans.StudentSponsorLetter;
import com.timsanalytics.crc.main.services.StudentSponsorLetterService;
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
@RequestMapping("/api/v1/student-sponsor-letter")
@Tag(name = "Student-Sponsor Letter", description = "Student-Sponsor Letter")
public class StudentSponsorLetterController {
    private final StudentSponsorLetterService studentSponsorLetterService;

    @Autowired
    public StudentSponsorLetterController(StudentSponsorLetterService studentSponsorLetterService) {
        this.studentSponsorLetterService = studentSponsorLetterService;
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor Letter List by Student ID", description = "Get Sponsor Letter List by Student ID", tags = {"Csi"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<StudentSponsorLetter>> getSponsorLetterListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.studentSponsorLetterService.getStudentSponsorLetterListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/sponsor/{sponsorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor Letter List by Student ID", description = "Get Sponsor Letter List by Student ID", tags = {"Csi"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<StudentSponsorLetter>> getSponsorLetterListBySponsorId(@Parameter(description = "Sponsor ID", required = true) @PathVariable Integer sponsorId) {
        try {
            return ResponseEntity.ok()
                    .body(this.studentSponsorLetterService.getStudentSponsorLetterListBySponsorId(sponsorId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
