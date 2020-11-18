package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.PostGradEvent;
import com.timsanalytics.crc.main.services.PostGradEventService;
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
@RequestMapping("/api/v1/post-grad-event")
@Tag(name = "Post-Grad Event", description = "Post-Grad Event")
public class PostGradEventController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final PostGradEventService postGradEventService;

    @Autowired
    public PostGradEventController(PostGradEventService postGradEventService) {
        this.postGradEventService = postGradEventService;
    }

    // BASIC CRUD

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Post-Grad Event", description = "Create Post-Grad Event", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostGradEvent> createPostGradEvent(@RequestBody PostGradEvent postGradEvent) {
        try {
            return ResponseEntity.ok()
                    .body(postGradEventService.createPostGradEvent(postGradEvent));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Post-Grad Event List", description = "Get Post-Grad Event List", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<PostGradEvent>> getPostGradEventList() {
        try {
            return ResponseEntity.ok()
                    .body(this.postGradEventService.getPostGradEventList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Post-Grad Event List (SSP)", description = "Get Post-Grad Event List (SSP)", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<PostGradEvent>> getPostGradEventList_SSP(@RequestBody ServerSidePaginationRequest<PostGradEvent> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<PostGradEvent> container = this.postGradEventService.getPostGradEventList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{postGradEventId}", method = RequestMethod.GET)
    @Operation(summary = "Get Post-Grad Event Detail", description = "Get Post-Grad Event Detail", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostGradEvent> getPostGradEventDetail(@Parameter(description = "Post-Grad Event ID", required = true) @PathVariable int postGradEventId) {
        try {
            PostGradEvent postGradEvent = postGradEventService.getPostGradEventDetail(postGradEventId);
            return ResponseEntity.ok()
                    .body(postGradEvent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Post-Grad Event", description = "Update Post-Grad Event", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<PostGradEvent> updatePostGradEvent(@RequestBody PostGradEvent postGradEvent) {
        try {
            return ResponseEntity.ok()
                    .body(postGradEventService.updatePostGradEvent(postGradEvent));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{postGradEventId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Post-Grad Event", description = "Delete Post-Grad Event", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deletePostGradEvent(@Parameter(description = "Post-Grad Event ID", required = true) @PathVariable String postGradEventId) {
        try {
            return ResponseEntity.ok()
                    .body(postGradEventService.deletePostGradEvent(postGradEventId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JOINED TABLES

    @ResponseBody
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Post-Grad List by Student ID", description = "Get Post-Grad List by Student ID", tags = {"Post-Grad Event"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<PostGradEvent>> getPostGradEventListByStudentId(@Parameter(description = "Student ID", required = true) @PathVariable Integer studentId) {
        try {
            return ResponseEntity.ok()
                    .body(this.postGradEventService.getPostGradEventListByStudentId(studentId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
