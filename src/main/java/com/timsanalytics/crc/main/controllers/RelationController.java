package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Relation;
import com.timsanalytics.crc.main.services.RelationService;
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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/relation")
@Tag(name = "Relation", description = "Relation")
public class RelationController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final RelationService relationService;

    @Autowired
    public RelationController(RelationService relationService) {
        this.relationService = relationService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Relation", tags = {"Relation"}, description = "Create Relation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relation> createRelation(@RequestBody Relation relation) {
        try {
            return ResponseEntity.ok()
                    .body(relationService.createRelation(relation));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Relation List (SSP)", tags = {"Relation"}, description = "Get Relation List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Relation>> getRelationList_SSP(@RequestBody ServerSidePaginationRequest<Relation> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Relation> container = this.relationService.getRelationList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @RequestMapping(value = "/{relationId}", method = RequestMethod.GET)
    @Operation(summary = "Get Relation Detail", tags = {"Relation"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relation> getRelationDetail(@Parameter(description = "Relation GUID", required = true) @PathVariable Integer relationId) {
        try {
            Relation relation = relationService.getRelationDetail(relationId);
            return ResponseEntity.ok()
                    .body(relation);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Relation", tags = {"Relation"}, description = "Update Relation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Relation> updateRelation(@RequestBody Relation relation) {
        try {
            return ResponseEntity.ok()
                    .body(relationService.updateRelation(relation));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{relationId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Relation", tags = {"Relation"}, description = "Delete Relation", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteRelation(@Parameter(description = "Relation GUID", required = true) @PathVariable String relationId) {
        try {
            return ResponseEntity.ok()
                    .body(relationService.deleteRelation(relationId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/student-relationship/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Student-Relation List", tags = {"Relation"}, description = "Student-Relation List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Relation>> getRelationListBystudentId(@Parameter(description = "Student GUID", required = true) @PathVariable String studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.relationService.getRelationListBystudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
