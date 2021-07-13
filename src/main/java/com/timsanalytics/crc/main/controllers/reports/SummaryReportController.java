package com.timsanalytics.crc.main.controllers.reports;

import com.timsanalytics.crc.main.beans.SummaryReportResult;
import com.timsanalytics.crc.main.services.reports.SummaryReportService;
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



//    @RequestMapping(value = "/active-students/results", method = RequestMethod.GET)
//    @Operation(summary = "Get Active Students Results", description = "Get Active Students Results", tags = {"Summary Report"}, security = @SecurityRequirement(name = "bearerAuth"))
//    public ResponseEntity<List<SummaryReportResult>> getActiveStudents_Results() {
//        try {
//            List<SummaryReportResult> count = summaryReportService.getActiveStudents_Results();
//            return ResponseEntity.ok()
//                    .body(count);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
