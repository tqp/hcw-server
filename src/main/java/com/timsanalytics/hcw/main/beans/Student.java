package com.timsanalytics.hcw.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {
    private String studentGuid;
    private String studentSurname;
    private String studentGivenName;
    private String studentGender;
    private String studentDateOfBirth;
    private String studentSchool;
    private String studentGrade;
    private String studentAddressCurrent;
    private String studentAddressPrevious;
    private String studentPhoneCurrent;
    private String studentPhonePrevious;
    // Joined Table Data
    private String tierTypeGuid;
    private String tierTypeName;
    // Metadata
    private String status;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;

    public String getStudentGuid() {
        return studentGuid;
    }

    public void setStudentGuid(String studentGuid) {
        this.studentGuid = studentGuid;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getStudentGivenName() {
        return studentGivenName;
    }

    public void setStudentGivenName(String studentGivenName) {
        this.studentGivenName = studentGivenName;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public String getStudentDateOfBirth() {
        return studentDateOfBirth;
    }

    public void setStudentDateOfBirth(String studentDateOfBirth) {
        this.studentDateOfBirth = studentDateOfBirth;
    }

    public String getStudentSchool() {
        return studentSchool;
    }

    public void setStudentSchool(String studentSchool) {
        this.studentSchool = studentSchool;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

    public String getStudentAddressCurrent() {
        return studentAddressCurrent;
    }

    public void setStudentAddressCurrent(String studentAddressCurrent) {
        this.studentAddressCurrent = studentAddressCurrent;
    }

    public String getStudentAddressPrevious() {
        return studentAddressPrevious;
    }

    public void setStudentAddressPrevious(String studentAddressPrevious) {
        this.studentAddressPrevious = studentAddressPrevious;
    }

    public String getStudentPhoneCurrent() {
        return studentPhoneCurrent;
    }

    public void setStudentPhoneCurrent(String studentPhoneCurrent) {
        this.studentPhoneCurrent = studentPhoneCurrent;
    }

    public String getStudentPhonePrevious() {
        return studentPhonePrevious;
    }

    public void setStudentPhonePrevious(String studentPhonePrevious) {
        this.studentPhonePrevious = studentPhonePrevious;
    }

    public String getTierTypeGuid() {
        return tierTypeGuid;
    }

    public void setTierTypeGuid(String tierTypeGuid) {
        this.tierTypeGuid = tierTypeGuid;
    }

    public String getTierTypeName() {
        return tierTypeName;
    }

    public void setTierTypeName(String tierTypeName) {
        this.tierTypeName = tierTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
