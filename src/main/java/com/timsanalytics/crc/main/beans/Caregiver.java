package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Caregiver {
    private int caregiverId;
    private String caregiverSurname;
    private String caregiverGivenName;
    private String caregiverGender;
    private String caregiverAddress;
    private String caregiverPhone;
    private String caregiverEmail;
    // Joined Tables
    private String relationshipEffectiveDate;
    private String relationshipType;
    private Boolean relationshipBloodRelative;
    private Integer studentCount;
    // Metadata
    private String status;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
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

    public String getCaregiverGender() {
        return caregiverGender;
    }

    public void setCaregiverGender(String caregiverGender) {
        this.caregiverGender = caregiverGender;
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

    public String getCaregiverEmail() {
        return caregiverEmail;
    }

    public void setCaregiverEmail(String caregiverEmail) {
        this.caregiverEmail = caregiverEmail;
    }

    public String getRelationshipEffectiveDate() {
        return relationshipEffectiveDate;
    }

    public void setRelationshipEffectiveDate(String relationshipEffectiveDate) {
        this.relationshipEffectiveDate = relationshipEffectiveDate;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Boolean getRelationshipBloodRelative() {
        return relationshipBloodRelative;
    }

    public void setRelationshipBloodRelative(Boolean relationshipBloodRelative) {
        this.relationshipBloodRelative = relationshipBloodRelative;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
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
