package com.timsanalytics.crc.main.controllers.people;

import com.timsanalytics.crc.common.beans.KeyValue;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.services.people.StudentService;
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
    @Operation(summary = "Create Student", description = "Create Student", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
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
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List", description = "Get Student List", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> getStudentList() {
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
    @Operation(summary = "Get Student Detail", description = "Get Student Detail", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Update Student", description = "Update Student", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
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
    @Operation(summary = "Delete Student", description = "Delete Student", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<KeyValue> deleteStudent(@Parameter(description = "Student GUID", required = true) @PathVariable String studentId) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.deleteStudent(studentId));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // FILTERED

    @ResponseBody
    @RequestMapping(value = "/sponsor/{sponsorId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List By Sponsor ID", description = "Get Student List By Sponsor ID", tags = {"Relationship"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> getStudentListBySponsorId(@Parameter(description = "Sponsor ID", required = true) @PathVariable Integer sponsorId) {
        try {
            return ResponseEntity.ok()
                    .body(this.studentService.getStudentListBySponsorId(sponsorId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // OTHER QUERIES

    @ResponseBody
    @RequestMapping(value = "/check-duplicate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Check Duplicate Student Record", description = "Check Duplicate Student Record", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> checkDuplicateStudentRecord(@RequestBody Student student) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.checkDuplicateStudentRecord(student));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
