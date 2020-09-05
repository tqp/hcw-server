package com.timsanalytics.crc.main.controllers;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.services.StudentService;
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

@RestController
@RequestMapping("/api/v1/student")
@Tag(name = "Student", description = "Student")
public class StudentController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Student", tags = {"Student"}, description = "Create Student", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.createStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List (SSP)", tags = {"Student"}, description = "Get Student List (SSP)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<Student>> getStudentList_SSP(@RequestBody ServerSidePaginationRequest<Student> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<Student> container = this.studentService.getStudentList_SSP(serverSidePaginationRequest);
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
    @Operation(summary = "Get Student Detail", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> getStudentDetail(@Parameter(description = "Student GUID", required = true) @PathVariable Integer studentId) {
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
    @Operation(summary = "Update Student", tags = {"Student"}, description = "Update Student", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.updateStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{studentId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete Student", tags = {"Student"}, description = "Delete Student", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteStudent(@Parameter(description = "Student GUID", required = true) @PathVariable String studentId) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.deleteStudent(studentId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
