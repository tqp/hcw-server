package com.timsanalytics.crc.auth.authCommon.controllers;

import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.auth.authCommon.services.UserService;
import com.timsanalytics.crc.common.beans.ServerSidePaginationRequest;
import com.timsanalytics.crc.common.beans.ServerSidePaginationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create User", description = "Create User", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> createUser(@RequestBody User user, HttpServletRequest request) {
        try {
            return ResponseEntity.ok()
                    .body(userService.createUser(user, this.tokenService.getUserFromRequest(request)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ssp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get User List (SSP)", description = "Get User List (SSP)", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ServerSidePaginationResponse<User>> getUserList_SSP(@RequestBody ServerSidePaginationRequest<User> serverSidePaginationRequest) {
        long startTime = new Date().getTime();
        try {
            ServerSidePaginationResponse<User> container = this.userService.getUserList_SSP(serverSidePaginationRequest);
            container.setRequestTime(new Date().getTime() - startTime);
            return ResponseEntity.ok()
                    .body(container);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @Operation(summary = "Get User Detail", description = "Get User Detail", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> getUserDetail(@Parameter(description = "User ID", required = true) @PathVariable Integer userId) {
        try {
            User user = userService.getUserDetail(userId);
            return ResponseEntity.ok()
                    .body(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    @Operation(summary = "Get User Detail by Username", description = "Get User Detail by Username", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> getUserDetailByUsername(@Parameter(description = "Username", required = true) @PathVariable String username) {
        try {
            User user = userService.getUserDetailByUsername(username);
            return ResponseEntity.ok()
                    .body(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update User", description = "Update User", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> updateUser(@RequestBody User user, HttpServletRequest request) {
        try {
            return ResponseEntity.ok()
                    .body(userService.updateUser(user, this.tokenService.getUserFromRequest(request)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete User", description = "Delete User", tags = {"User"}, security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<User> deleteUser(@Parameter(description = "User ID", required = true) @PathVariable Integer userId,
                                           HttpServletRequest request) {
        try {
            return ResponseEntity.ok()
                    .body(userService.deleteUser(userId, this.tokenService.getUserFromRequest(request)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
