package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.services.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Reports", description = "Reports")
public class ReportsController {
    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @RequestMapping(value = "/case-manager-coverage", method = RequestMethod.GET)
    @Operation(summary = "Case Manager Coverage Report", description = "Case Manager Coverage Report", tags = {"Reports"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getCaseManagerCoverageReport() {
        try {
            Integer count = reportsService.getCaseManagerCoverageReport();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
