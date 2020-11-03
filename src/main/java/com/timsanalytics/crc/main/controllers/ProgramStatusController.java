package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.ProgramStatus;
import com.timsanalytics.crc.main.beans.ProgramStatusPackage;
import com.timsanalytics.crc.main.beans.Sponsor;
import com.timsanalytics.crc.main.services.ProgramStatusService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/program-status")
@Tag(name = "Program Status", description = "Program Status")
public class ProgramStatusController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final ProgramStatusService programStatusService;

    @Autowired
    public ProgramStatusController(ProgramStatusService programStatusService) {
        this.programStatusService = programStatusService;
    }

    @ResponseBody
    @RequestMapping(value = "/{programStatusId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Program Status Package", description = "Program Status Package", tags = {"Program Status"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProgramStatusPackage> getProgramStatusPackage(@Parameter(description = "Program Status ID", required = true) @PathVariable int programStatusId) {
        try {
            return ResponseEntity.ok()
                    .body(this.programStatusService.getProgramStatusPackage(programStatusId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/child-list/{programStatusId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Program Status Child List", description = "Program Status Child List", tags = {"Program Status"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ProgramStatusPackage>> getProgramStatusChildList(@Parameter(description = "Program Status ID", required = true) @PathVariable int programStatusId) {
        try {
            return ResponseEntity.ok()
                    .body(this.programStatusService.getProgramStatusChildList(programStatusId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // JOINED QUERIES

    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    @Operation(summary = "Get Program Status Detail by Student ID", description = "Get Program Status Detail by Student ID", tags = {"Program Status"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ProgramStatus> getSponsorDetailByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable int studentId) {
        try {
            ProgramStatus programStatus = programStatusService.getProgramStatusDetailByStudentId(studentId);
            return ResponseEntity.ok()
                    .body(programStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
