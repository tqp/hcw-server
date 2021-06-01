package com.timsanalytics.crc.main.controllers.finance;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Payment;
import com.timsanalytics.crc.main.services.PaymentService;
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
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment", description = "Payment")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Add Payment", description = "Add Payment", tags = {"Payment"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment) {
        try {
            return ResponseEntity.ok()
                    .body(paymentService.addPayment(payment));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/payment-list/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Payment List", description = "Payment List", tags = {"Payment"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Payment>> getPaymentList_SSP(@RequestBody ServerSidePaginationRequest<Payment> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Payment> container = this.paymentService.getPaymentList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{paymentId}", method = RequestMethod.GET)
    @Operation(summary = "Get Payment Detail", description = "Get Payment Detail", tags = {"Payment"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> getPaymentDetail(@Parameter(description = "Payment ID", required = true) @PathVariable int paymentId) {
        try {
            Payment payment = paymentService.getPaymentDetail(paymentId);
            return ResponseEntity.ok()
                    .body(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Payment", description = "Update Payment", tags = {"Payment"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
        try {
            return ResponseEntity.ok()
                    .body(paymentService.updatePayment(payment));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{paymentId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Payment", description = "Delete Payment", tags = {"Payment"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deletePayment(@Parameter(description = "Payment ID", required = true) @PathVariable String paymentId) {
        try {
            return ResponseEntity.ok()
                    .body(paymentService.deletePayment(paymentId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
