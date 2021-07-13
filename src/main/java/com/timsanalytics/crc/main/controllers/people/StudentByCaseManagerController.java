package com.timsanalytics.crc.main.controllers.people;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.main.beans.Student;
import com.timsanalytics.crc.main.services.people.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student-by-case-manager")
@Tag(name = "Student by Case Manager", description = "Student by Case Manager")
public class StudentByCaseManagerController {
    private final StudentService studentService;
    private final TokenService tokenService;

    @Autowired
    public StudentByCaseManagerController(StudentService studentService, TokenService tokenService) {
        this.studentService = studentService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get Student List By Case Manager", description = "Get Student List By Case Manager", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<Student>> getStudentListByCaseManager(HttpServletRequest request) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        try {
            return ResponseEntity.ok()
                    .body(this.studentService.getStudentListByCaseManager(loggedInUser));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Student By Case Manager", description = "Update Student By Case Manager", tags = {"Student"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Student> updateStudentByCaseManagerList(@RequestBody Student student) {
        try {
            return ResponseEntity.ok()
                    .body(studentService.updateStudent(student));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
