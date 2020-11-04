package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.main.beans.InteractionType;
import com.timsanalytics.crc.main.services.InteractionTypeService;
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
@RequestMapping("/api/v1/interaction-type")
@Tag(name = "Interaction Type", description = "Interaction Type")
public class InteractionTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final InteractionTypeService interactionTypeService;

    @Autowired
    public InteractionTypeController(InteractionTypeService interactionTypeService) {
        this.interactionTypeService = interactionTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Interaction Type List", tags = {"Interaction Type"}, description = "Interaction Type List", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<InteractionType>> getInteractionTypeList() {
        try {
            return ResponseEntity.ok()
                    .body(this.interactionTypeService.getInteractionTypeList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
