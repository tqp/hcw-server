package com.timsanalytics.crc.main.controllers.reports;

import com.timsanalytics.crc.main.services.reports.SummaryReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/summary-report")
@Tag(name = "Summary Report", description = "Summary Report")
public class SummaryReportController {
    private final SummaryReportService summaryReportService;

    @Autowired
    public SummaryReportController(SummaryReportService summaryReportService) {
        this.summaryReportService = summaryReportService;
    }

    @RequestMapping(value = "/student-count-total", method = RequestMethod.GET)
    @Operation(summary = "student-count-total", description = "student-count-total", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getStudentCountTotal() {
        try {
            Integer count = summaryReportService.getStudentCountTotal();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/student-count-reintegrated", method = RequestMethod.GET)
    @Operation(summary = "student-count-reintegrated", description = "student-count-reintegrated", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getStudentCountReintegrated() {
        try {
            Integer count = summaryReportService.getStudentCountReintegrated();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/student-count-reintegrated-runaway", method = RequestMethod.GET)
    @Operation(summary = "student-count-reintegrated-runaway", description = "student-count-reintegrated-runaway", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getStudentCountReintegratedRunaway() {
        try {
            Integer count = summaryReportService.getStudentCountReintegratedRunaway();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/student-count-families-intact", method = RequestMethod.GET)
    @Operation(summary = "student-count-families-intact", description = "student-count-families-intact", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getStudentCountFamiliesIntact() {
        try {
            Integer count = summaryReportService.getStudentCountFamiliesIntact();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/student-count-families-intact-enrolled", method = RequestMethod.GET)
    @Operation(summary = "student-count-families-intact-enrolled", description = "student-count-families-intact-enrolled", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getStudentCountFamiliesIntactEnrolled() {
        try {
            Integer count = summaryReportService.getStudentCountFamiliesIntactEnrolled();
            return ResponseEntity.ok()
                    .body(count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
