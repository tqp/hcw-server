package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Csi;
import com.timsanalytics.crc.main.beans.CsiScoresReport;
import com.timsanalytics.crc.main.services.CsiScoresReportService;
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
@RequestMapping("/api/v1/csi-scores-report")
@Tag(name = "CSI Scores Report", description = "CSI Scores Report")
public class CsiScoresReportController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final CsiScoresReportService csiScoresReportService;

    @Autowired
    public CsiScoresReportController(CsiScoresReportService csiScoresReportService) {
        this.csiScoresReportService = csiScoresReportService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Csi Scores Report Data", description = "Get Csi Scores Report Data", tags = {"CSI Scores Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CsiScoresReport>> getCsiScoresReportData() {
        try {
            return ResponseEntity.ok()
                    .body(this.csiScoresReportService.getCsiScoresReportData());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
