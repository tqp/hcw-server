package com.timsanalytics.crc.auth.authCommon.controllers;

import com.timsanalytics.crc.auth.authCommon.beans.Role;
import com.timsanalytics.crc.auth.authCommon.beans.User;
import com.timsanalytics.crc.auth.authCommon.services.TokenService;
import com.timsanalytics.crc.auth.authCommon.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/my-profile")
@Tag(name = "My Profile", description = "My Profile")
public class MyProfileController {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());
    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public MyProfileController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // READ: ITEM
    @RequestMapping(value = "", method = RequestMethod.GET)
    @Operation(summary = "Get My Profile", description = "Get My Profile", tags = {"My Profile"}, security = @SecurityRequirement(name = "bearerAuth"))
    public User getMyProfile(HttpServletRequest request) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        this.logger.trace("MyProfileController -> getMyProfile: userGuid=" + loggedInUser.getUserId());
        try {
            return this.userService.getUserDetail(loggedInUser.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // UPDATE
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @Operation(summary = "Update My Profile", description = "Update My Profile", tags = {"My Profile"})
    public User updateMyProfile(HttpServletRequest request, @RequestBody User User) {
        this.logger.debug("############################################################################");
        this.logger.debug("MyProfileController -> updateMyProfile: userGuid=" + User.getUserId());
        try {
            User loggedInUser = this.tokenService.getUserFromRequest(request);
            this.logger.debug("loggedInUser=" + loggedInUser.getUserId());
            return this.userService.updateMyProfile(User, loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // OTHER CRUD

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @Operation(summary = "Get My User Roles", description = "Get My User Roles", tags = {"My Profile"})
    public List<Role> getMyUserRoles(HttpServletRequest request) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        try {
            return this.userService.getUserRoles(loggedInUser.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/update-password", method = RequestMethod.PUT)
    @Operation(summary = "Update My Password", description = "Update My Password", tags = {"My Profile"})
    public User updatePassword(HttpServletRequest request, @RequestBody User User) {
        try {
            User loggedInUser = this.tokenService.getUserFromRequest(request);
            return this.userService.updatePassword(User, loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/confirm-password", method = RequestMethod.PUT)
    @Operation(summary = "Confirm whether the provided password matches my password", description = "Confirm whether the provided password matches my password", tags = {"My Profile"})
    public Boolean confirmUserPassword(HttpServletRequest request, @RequestBody User testPassword) {
        User loggedInUser = this.tokenService.getUserFromRequest(request);
        User User = this.userService.getUserDetail(loggedInUser.getUserId());
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(testPassword.getPassword(), User.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // NON-CRUD CONTROLLERS

    @RequestMapping(value = "/username/{username}", method = RequestMethod.GET)
    @Operation(summary = "Get the User ID for a given Username", description = "Get the User ID for a given Username", tags = {"My Profile"})
    public Integer getUserGuidByUsername(@PathVariable String username) {
        this.logger.debug(("UserController -> getUserIdByUsername: username=" + username));
        try {
            return this.userService.getUserGuidByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/{userGuid}/roles", method = RequestMethod.GET)
    @Operation(summary = "Get User Roles by User GUID", description = "Get User Roles by User GUID", tags = {"My Profile"})
    public List<Role> getUserRoles(@PathVariable Integer userGuid) {
        this.logger.debug(("UserController -> getUserRoles: userGuid=" + userGuid));
        try {
            return this.userService.getUserRoles(userGuid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
