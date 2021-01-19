package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.CaseManagerRevised;
import com.timsanalytics.crc.main.services.CaseManagerRevisedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/case-manager-revised")
@Tag(name = "Case Manager (Rev)", description = "Case Manager (Rev)")
public class CaseManagerRevisedController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CaseManagerRevisedService caseManagerRevisedService;

    @Autowired
    public CaseManagerRevisedController(CaseManagerRevisedService caseManagerRevisedService) {
        this.caseManagerRevisedService = caseManagerRevisedService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Case Manager Revised List", description = "Get Case Manager Revised List", tags = {"Case Manager Revised"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CaseManagerRevised>> getCaseManagerRevisedList() {
        try {
            return ResponseEntity.ok()
                    .body(this.caseManagerRevisedService.getCaseManagerRevisedList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
