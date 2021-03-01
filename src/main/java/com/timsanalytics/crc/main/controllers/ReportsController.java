package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.services.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<List<Student>> getCaseManagerCoverageReport() {
        try {
            return ResponseEntity.ok()
                    .body(reportsService.getCaseManagerCoverageReport());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/caregiver-coverage", method = RequestMethod.GET)
    @Operation(summary = "Caregiver Coverage Report", description = "Caregiver Coverage Report", tags = {"Reports"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> getCaregiverCoverageReport() {
        try {
            return ResponseEntity.ok()
                    .body(reportsService.getCaregiverCoverageReport());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
