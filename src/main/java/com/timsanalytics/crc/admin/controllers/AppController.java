package com.timsanalytics.crc.admin.controllers;

import com.timsanalytics.crc.admin.services.AppService;
import com.timsanalytics.crc.auth.authCommon.beans.KeyValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/health-check")
@Tag(name = "App", description = "This is used to query application health-related data.")
public class AppController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final AppService appService;
    private final Environment environment;

    @Autowired
    public AppController(AppService appService,
                         Environment environment) {
        this.appService = appService;
        this.environment = environment;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Health Check Response", description = "Get Health Check Response", tags = {"App"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> getServerHealthCheck() {
        try {
            return ResponseEntity.ok()
                    .body(new KeyValue("health-check", "success"));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/database-health-check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Database Health Check Response", description = "Get Database Health Check Response", tags = {"App"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Integer> getDatabaseHealthCheck() {
        try {
            return ResponseEntity.ok()
                    .body(this.appService.getDatabaseHealthCheck());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/build-timestamp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Build Timestamp", tags = {"App"}, description = "Gets the date-time when the server was last built.", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> getBuildTimestamp() {
        this.logger.trace("AppController -> getBuildTimestamp");
        try {
            return ResponseEntity.ok()
                    .body(new KeyValue("buildTimestamp", this.appService.getBuildTimestamp()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Environment Profile", description = "Get Environment Profile", tags = {"Environment"})
    public ResponseEntity<KeyValue> getEnvironmentProfile() {
        try {
            return ResponseEntity.ok()
                    .body(new KeyValue("application.environment", this.environment.getProperty("application.environment")));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
