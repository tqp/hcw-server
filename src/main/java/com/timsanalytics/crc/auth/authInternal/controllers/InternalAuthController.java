package com.timsanalytics.crc.auth.authInternal.controllers;

import com.timsanalytics.crc.auth.authCommon.services.AuthenticationException;
import com.timsanalytics.crc.auth.authInternal.beans.LoginUser;
import com.timsanalytics.crc.auth.authInternal.services.InternalAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/internal")
@Tag(name = "Authentication - Internal", description = "Internal Authentication")
public class InternalAuthController {
    private final InternalAuthService internalAuthService;

    @Autowired
    public InternalAuthController(InternalAuthService internalAuthService) {
        this.internalAuthService = internalAuthService;
    }

    // TODO: Verify csrf state token
    @RequestMapping(value = "", method = RequestMethod.POST)
    @Operation(summary = "Authenticate with User Object", description = "Attempt authentication using a given LoginUser object", tags = {"Authentication - Internal"})
    public ResponseEntity<?> attemptAuthentication(@RequestBody LoginUser loginUser) {
        return this.internalAuthService.attemptAuthentication(loginUser);
    }

    @RequestMapping(value = "/{a_username}/{b_password}", method = RequestMethod.POST)
    @Operation(summary = "Authenticate with Username and Password", description = "Attempt authentication using a given Username and Password", tags = {"Authentication - Internal"})
    public ResponseEntity<?> submitUsernameAndPassword(@Parameter(description="Username", required=true, example = "user") @PathVariable String a_username,
                                                       @Parameter(description="Password", required=true, example = "user1") @PathVariable String b_password) {
        return this.internalAuthService.submitUsernameAndPassword(a_username, b_password);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

}
