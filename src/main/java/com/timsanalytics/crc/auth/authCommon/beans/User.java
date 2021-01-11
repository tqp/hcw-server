package com.timsanalytics.crc.auth.authCommon.beans;

import java.util.Date;
import java.util.List;

public class User {
    private Integer userId;
    private String username;
    private String surname;
    private String givenName;
    private String password;
    private String passwordSet;
    private Integer passwordReset;
    private String lastLogin;
    private Integer loginCount;
    private Integer status;
    private Date createdOn;
    private String createdBy;
    private Date updatedOn;
    private String updatedBy;
    private String theme;
    private String picture;
    private List<Role> roles;
    private String rolesString;

    // ROLES
    private int RoleUser;
    private int RoleCaseManager;
    private int RoleMonitoring;
    private int RoleDirector;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSet() {
        return passwordSet;
    }

    public void setPasswordSet(String passwordSet) {
        this.passwordSet = passwordSet;
    }

    public Integer getPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(Integer passwordReset) {
        this.passwordReset = passwordReset;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getRolesString() {
        return rolesString;
    }

    public void setRolesString(String rolesString) {
        this.rolesString = rolesString;
    }

    public int getRoleUser() {
        return RoleUser;
    }

    public void setRoleUser(int roleUser) {
        RoleUser = roleUser;
    }

    public int getRoleCaseManager() {
        return RoleCaseManager;
    }

    public void setRoleCaseManager(int roleCaseManager) {
        RoleCaseManager = roleCaseManager;
    }

    public int getRoleMonitoring() {
        return RoleMonitoring;
    }

    public void setRoleMonitoring(int roleMonitoring) {
        RoleMonitoring = roleMonitoring;
    }

    public int getRoleDirector() {
        return RoleDirector;
    }

    public void setRoleDirector(int roleDirector) {
        RoleDirector = roleDirector;
    }
}
