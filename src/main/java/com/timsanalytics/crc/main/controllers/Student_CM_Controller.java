package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.services.Student_CM_Service;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student-by-case-manager")
@Tag(name = "Student by Case Manager", description = "Student by Case Manager")
public class Student_CM_Controller {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final Student_CM_Service studentService;
    private final TokenService tokenService;

    @Autowired
    public Student_CM_Controller(Student_CM_Service studentService, TokenService tokenService) {
        this.studentService = studentService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List", description = "Get Student List", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> getStudent_CM_List() {
        try {
            return ResponseEntity.ok()
                    .body(this.studentService.getStudentList());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List (SSP)", description = "Get Student List (SSP)", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Student>> getStudent_CM_List_SSP(
            @RequestBody ServerSidePaginationRequest<Student> serverSidePaginationRequest,
            HttpServletRequest request) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Student> container = this.studentService.getStudentList_SSP(serverSidePaginationRequest, loggedInUser.getUserId());
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @RequestMapping(value = "/{studentId}", method = RequestMethod.GET)
    @Operation(summary = "Get Student Detail", description = "Get Student Detail", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> getStudent_CM_Detail(@Parameter(description = "Student GUID", required = true) @PathVariable Integer studentId) {
        try {
            Student student = studentService.getStudentDetail(studentId);
            return ResponseEntity.ok()
                    .body(student);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Student", description = "Update Student", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> updateStudent_CM(@RequestBody Student student) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.updateStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
