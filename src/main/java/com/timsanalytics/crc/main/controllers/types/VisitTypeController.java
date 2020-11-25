package com.timsanalytics.crc.main.controllers.types;

import com.timsanalytics.crc.main.beans.types.VisitType;
import com.timsanalytics.crc.main.services.types.VisitTypeService;
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
@RequestMapping("/api/v1/visit-type")
@Tag(name = "Visit Type", description = "Visit Type")
public class VisitTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final VisitTypeService visitTypeService;

    @Autowired
    public VisitTypeController(VisitTypeService visitTypeService) {
        this.visitTypeService = visitTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Visit Type List", tags = {"Visit Type"}, description = "Visit Type List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<VisitType>> getVisitTypeList() {
        try {
            return ResponseEntity.ok()
                    .body(this.visitTypeService.getVisitTypeList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
