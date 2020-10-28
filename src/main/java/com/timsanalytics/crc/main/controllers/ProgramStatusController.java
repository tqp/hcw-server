package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.ProgramStatus;
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
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Program Status List", tags = {"Program Status"}, description = "Program Status List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ProgramStatus>> getProgramStatusList() {
        try {
            return ResponseEntity.ok()
                    .body(this.programStatusService.getProgramStatusList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/top-level", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Program Status List: Top Level", tags = {"Program Status"}, description = "Program Status List:Top Level", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ProgramStatus>> getProgramStatusTopLevelList() {
        try {
            return ResponseEntity.ok()
                    .body(this.programStatusService.getProgramStatusTopLevelList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/children/{parentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Program Status List: Child List", tags = {"Program Status"}, description = "Program Status List: Child List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ProgramStatus>> getProgramStatusChildList(@Parameter(description = "Program Status Parent ID", required = true) @PathVariable int parentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.programStatusService.getProgramStatusChildList(parentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
