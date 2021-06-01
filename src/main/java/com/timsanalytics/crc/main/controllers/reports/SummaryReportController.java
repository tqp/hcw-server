package com.timsanalytics.crc.main.controllers.reports;

import com.timsanalytics.crc.main.beans.SummaryReportResult;
import com.timsanalytics.crc.main.services.SummaryReportService;
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
@RequestMapping("/api/v1/summary-report")
@Tag(name = "Summary Report", description = "Summary Report")
public class SummaryReportController {
    private final SummaryReportService summaryReportService;

    @Autowired
    public SummaryReportController(SummaryReportService summaryReportService) {
        this.summaryReportService = summaryReportService;
    }

    @RequestMapping(value = "/active-students/count", method = RequestMethod.GET)
    @Operation(summary = "Get Active Students Count", description = "Get Active Students Count", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getActiveStudents_Count() {
        try {
            Integer count = summaryReportService.getActiveStudents_Count();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/active-students/results", method = RequestMethod.GET)
    @Operation(summary = "Get Active Students Results", description = "Get Active Students Results", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<SummaryReportResult>> getActiveStudents_Results() {
        try {
            List<SummaryReportResult> count = summaryReportService.getActiveStudents_Results();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
