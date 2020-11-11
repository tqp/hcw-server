package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Student {
    private Integer studentId;
    private String studentSurname;
    private String studentGivenName;
    private String studentGender;
    private String studentDateOfBirth;
    private String studentSchool;
    private String studentGrade;
    private Integer schoolLevelTypeId;
    private Integer classLevelTypeId;
    private Integer classRepeatYearTypeId;
    private Integer impairmentTypeId;
    // Joined Table Data
    private Integer caregiverId;
    private String caregiverSurname;
    private String caregiverGivenName;
    private String caregiverAddress;
    private String caregiverPhone;
    private Integer relationshipTierTypeId;
    private String relationshipTierTypeName;
    private String schoolLevelTypeName;
    private String classLevelTypeName;
    private String impairmentTypeName;
    // Metadata
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;
    private String deleted;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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

    public Integer getSchoolLevelTypeId() {
        return schoolLevelTypeId;
    }

    public void setSchoolLevelTypeId(Integer schoolLevelTypeId) {
        this.schoolLevelTypeId = schoolLevelTypeId;
    }

    public Integer getClassLevelTypeId() {
        return classLevelTypeId;
    }

    public void setClassLevelTypeId(Integer classLevelTypeId) {
        this.classLevelTypeId = classLevelTypeId;
    }

    public Integer getClassRepeatYearTypeId() {
        return classRepeatYearTypeId;
    }

    public void setClassRepeatYearTypeId(Integer classRepeatYearTypeId) {
        this.classRepeatYearTypeId = classRepeatYearTypeId;
    }

    public Integer getImpairmentTypeId() {
        return impairmentTypeId;
    }

    public void setImpairmentTypeId(Integer impairmentTypeId) {
        this.impairmentTypeId = impairmentTypeId;
    }

    public Integer getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Integer caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getCaregiverSurname() {
        return caregiverSurname;
    }

    public void setCaregiverSurname(String caregiverSurname) {
        this.caregiverSurname = caregiverSurname;
    }

    public String getCaregiverGivenName() {
        return caregiverGivenName;
    }

    public void setCaregiverGivenName(String caregiverGivenName) {
        this.caregiverGivenName = caregiverGivenName;
    }

    public String getCaregiverAddress() {
        return caregiverAddress;
    }

    public void setCaregiverAddress(String caregiverAddress) {
        this.caregiverAddress = caregiverAddress;
    }

    public String getCaregiverPhone() {
        return caregiverPhone;
    }

    public void setCaregiverPhone(String caregiverPhone) {
        this.caregiverPhone = caregiverPhone;
    }

    public Integer getRelationshipTierTypeId() {
        return relationshipTierTypeId;
    }

    public void setRelationshipTierTypeId(Integer relationshipTierTypeId) {
        this.relationshipTierTypeId = relationshipTierTypeId;
    }

    public String getRelationshipTierTypeName() {
        return relationshipTierTypeName;
    }

    public void setRelationshipTierTypeName(String relationshipTierTypeName) {
        this.relationshipTierTypeName = relationshipTierTypeName;
    }

    public String getSchoolLevelTypeName() {
        return schoolLevelTypeName;
    }

    public void setSchoolLevelTypeName(String schoolLevelTypeName) {
        this.schoolLevelTypeName = schoolLevelTypeName;
    }

    public String getClassLevelTypeName() {
        return classLevelTypeName;
    }

    public void setClassLevelTypeName(String classLevelTypeName) {
        this.classLevelTypeName = classLevelTypeName;
    }

    public String getImpairmentTypeName() {
        return impairmentTypeName;
    }

    public void setImpairmentTypeName(String impairmentTypeName) {
        this.impairmentTypeName = impairmentTypeName;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
