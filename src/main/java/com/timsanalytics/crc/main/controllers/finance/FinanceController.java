package com.timsanalytics.crc.main.controllers.finance;

import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.services.FinanceService;
import com.timsanalytics.crc.main.services.LoanService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/finance")
@Tag(name = "Finance", description = "Finance")
public class FinanceController {
    private final FinanceService financeService;
    private final LoanService loanService;

    @Autowired
    public FinanceController(FinanceService financeService,
                             LoanService loanService) {
        this.financeService = financeService;
        this.loanService = loanService;
    }

    // REPORTING

    @ResponseBody
    @RequestMapping(value = "/report-by-participant/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Loan List", description = "Loan List", tags = {"Finance"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Loan>> getLoanList_SSP(@RequestBody ServerSidePaginationRequest<Loan> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Loan> container = this.loanService.getLoanList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "/total-committed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Total Committed", description = "Total Committed", tags = {"Finance"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Double> getTotalCommitted() {
        try {
            return ResponseEntity.ok()
                    .body(financeService.getTotalCommitted());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/total-paid", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Total Paid", description = "Total Paid", tags = {"Finance"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Double> getTotalPaid() {
        try {
            return ResponseEntity.ok()
                    .body(financeService.getTotalPaid());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // LOANS

    @ResponseBody
    @RequestMapping(value = "/loan/caregiver/{caregiverId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Sponsor List", tags = {"Sponsor"}, description = "Get Sponsor List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Loan>> getLoanListByCaregiverId(@Parameter(description = "Caregiver ID", required = true) @PathVariable int caregiverId) {
        try {
            return ResponseEntity.ok()
                    .body(this.financeService.getLoanListByCaregiverId(caregiverId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
