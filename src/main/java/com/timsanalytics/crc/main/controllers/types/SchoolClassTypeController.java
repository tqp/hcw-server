package com.timsanalytics.crc.main.controllers.types;

import com.timsanalytics.crc.main.beans.types.SchoolClassType;
import com.timsanalytics.crc.main.services.types.SchoolClassTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/school-class-type")
@Tag(name = "School-Class Type", description = "School-Class Type")
public class SchoolClassTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final SchoolClassTypeService schoolClassTypeService;

    @Autowired
    public SchoolClassTypeController(SchoolClassTypeService schoolClassTypeService) {
        this.schoolClassTypeService = schoolClassTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "/child-list/{schoolClassTypeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "School Class Child List", description = "School Class Child List", tags = {"School-Class Type"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<SchoolClassType>> getSchoolClassTypeChildList(@Parameter(description = "School Class Type ID", required = true) @PathVariable int schoolClassTypeId) {
        try {
            return ResponseEntity.ok()
                    .body(this.schoolClassTypeService.getSchoolClassTypeChildList(schoolClassTypeId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
