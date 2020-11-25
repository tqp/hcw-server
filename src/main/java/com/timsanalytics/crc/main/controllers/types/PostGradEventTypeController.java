package com.timsanalytics.crc.main.controllers.types;

import com.timsanalytics.crc.main.beans.types.PostGradEventType;
import com.timsanalytics.crc.main.services.types.PostGradEventTypeService;
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
@RequestMapping("/api/v1/post-grad-event-type")
@Tag(name = "Post-Grad Event Type", description = "Post-Grad Event Type")
public class PostGradEventTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final PostGradEventTypeService postGradEventTypeService;

    @Autowired
    public PostGradEventTypeController(PostGradEventTypeService postGradEventTypeService) {
        this.postGradEventTypeService = postGradEventTypeService;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Post-Grad Event Type List", description = "Post-Grad Event Type List", tags = {"Post-Grad Event Type"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<PostGradEventType>> getPostGradEventTypeList() {
        try {
            return ResponseEntity.ok()
                    .body(this.postGradEventTypeService.getPostGradEventTypeList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
