package com.timsanalytics.crc.main.controllers.events;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.main.beans.Visit;
import com.timsanalytics.crc.main.services.events.VisitService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visit-by-case-manager")
@Tag(name = "Visit by Case Manager", description = "Visit by Case Manager")
public class VisitByCaseManagerController {
    private final VisitService visitService;
    private final TokenService tokenService;

    @Autowired
    public VisitByCaseManagerController(VisitService visitService, TokenService tokenService) {
        this.visitService = visitService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Visit List By Case Manager", description = "Get Visit List By Case Manager", tags = {"Visit by Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Visit>> getVisitListByCaseManager(HttpServletRequest request) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        try {
            return ResponseEntity.ok()
                    .body(this.visitService.getVisitListByCaseManager(loggedInUser));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get My Visit List by Student", description = "Get My Visit List by Student", tags = {"Visit by Case Manager"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Visit>> getVisitListByCaseManagerAndStudent(HttpServletRequest request,
                                                                           @Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        try {
            return ResponseEntity.ok()
                    .body(this.visitService.getVisitListByCaseManagerAndStudent(loggedInUser, studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
