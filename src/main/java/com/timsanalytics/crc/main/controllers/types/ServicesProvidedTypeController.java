package com.timsanalytics.crc.main.controllers.types;

import com.timsanalytics.crc.main.beans.types.ServicesProvidedType;
import com.timsanalytics.crc.main.services.types.ServicesProvidedTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/services-provided-type")
@Tag(name = "Services Provided Type", description = "Services Provided Type")
public class ServicesProvidedTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final ServicesProvidedTypeService servicesProvidedTypeService;

    @Autowired
    public ServicesProvidedTypeController(ServicesProvidedTypeService servicesProvidedTypeService) {
        this.servicesProvidedTypeService = servicesProvidedTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Services Provided  Type List", description = "Services Provided  Type List", tags = {"Services Provided  Type"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ServicesProvidedType>> getServicesProvidedTypeList() {
        try {
            return ResponseEntity.ok()
                    .body(this.servicesProvidedTypeService.getServicesProvidedTypeList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
