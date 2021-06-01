package com.timsanalytics.crc.main.controllers.finance;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Loan;
import com.timsanalytics.crc.main.beans.Sponsor;
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

@RestController
@RequestMapping("/api/v1/loan")
@Tag(name = "Loan", description = "Loan")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Loan", description = "Create Loan", tags = {"Loan"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        try {
            return ResponseEntity.ok()
                    .body(loanService.createLoan(loan));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/loan-list/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Loan List", description = "Get Loan List", tags = {"Loan"}, security = @SecurityRequirement(name = "bearerAuth"))
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

    @RequestMapping(value = "/{loanId}", method = RequestMethod.GET)
    @Operation(summary = "Get Loan Detail", description = "Get Loan Detail", tags = {"Loan"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Loan> getLoanDetail(@Parameter(description = "Loan ID", required = true) @PathVariable int loanId) {
        try {
            Loan loan = loanService.getLoanDetail(loanId);
            return ResponseEntity.ok()
                    .body(loan);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Loan", description = "Update Loan", tags = {"Loan"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Loan> updateLoan(@RequestBody Loan loan) {
        try {
            return ResponseEntity.ok()
                    .body(loanService.updateLoan(loan));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{loanId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Loan", description = "Delete Loan", tags = {"Loan"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteLoan(@Parameter(description = "Loan ID", required = true) @PathVariable String loanId) {
        try {
            return ResponseEntity.ok()
                    .body(loanService.deleteLoan(loanId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
