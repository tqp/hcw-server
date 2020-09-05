package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.RelationshipType;
import com.timsanalytics.crc.main.beans.TierType;
import com.timsanalytics.crc.main.services.RelationshipTypeService;
import com.timsanalytics.crc.main.services.TierTypeService;
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
@RequestMapping("/api/v1/relationship-type")
@Tag(name = "Relationship Type", description = "Relationship Type")
public class RelationshipTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final RelationshipTypeService relationshipTypeService;

    @Autowired
    public RelationshipTypeController(RelationshipTypeService relationshipTypeService) {
        this.relationshipTypeService = relationshipTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Tier Type List", tags = {"Tier Type"}, description = "Tier Type List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<RelationshipType>> getRelationshipTypeList() {
        try {
            return ResponseEntity.ok()
                    .body(this.relationshipTypeService.getRelationshipTypeList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
