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
    private Integer position;
    private List<Role> roles;
    private String rolesString;

    // ROLES
    private int roleView;
    private int roleManager;
    private int roleCaseManager;
    private int roleCreate;
    private int roleEdit;
    private int roleReports;
    private int roleFinance;

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

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public int getRoleView() {
        return roleView;
    }

    public void setRoleView(int roleView) {
        this.roleView = roleView;
    }

    public int getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(int roleManager) {
        this.roleManager = roleManager;
    }

    public int getRoleCaseManager() {
        return roleCaseManager;
    }

    public void setRoleCaseManager(int roleCaseManager) {
        this.roleCaseManager = roleCaseManager;
    }

    public int getRoleCreate() {
        return roleCreate;
    }

    public void setRoleCreate(int roleCreate) {
        this.roleCreate = roleCreate;
    }

    public int getRoleEdit() {
        return roleEdit;
    }

    public void setRoleEdit(int roleEdit) {
        this.roleEdit = roleEdit;
    }

    public int getRoleReports() {
        return roleReports;
    }

    public void setRoleReports(int roleReports) {
        this.roleReports = roleReports;
    }

    public int getRoleFinance() {
        return roleFinance;
    }

    public void setRoleFinance(int roleFinance) {
        this.roleFinance = roleFinance;
    }
}
