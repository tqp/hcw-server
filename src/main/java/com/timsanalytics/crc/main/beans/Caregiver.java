package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

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
    private String relationshipStartDate;
    private String relationshipEndDate;
    private Integer relationshipTypeID;
    private String relationshipTypeName;
    private Integer relationshipTierTypeID;
    private String relationshipTierTypeName;
    private Boolean relationshipBloodRelative;
    private Integer studentCount;
    private Integer familyOfOriginTypeId;
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

    public String getRelationshipStartDate() {
        return relationshipStartDate;
    }

    public void setRelationshipStartDate(String relationshipStartDate) {
        this.relationshipStartDate = relationshipStartDate;
    }

    public String getRelationshipEndDate() {
        return relationshipEndDate;
    }

    public void setRelationshipEndDate(String relationshipEndDate) {
        this.relationshipEndDate = relationshipEndDate;
    }

    public Integer getRelationshipTypeID() {
        return relationshipTypeID;
    }

    public void setRelationshipTypeID(Integer relationshipTypeID) {
        this.relationshipTypeID = relationshipTypeID;
    }

    public String getRelationshipTypeName() {
        return relationshipTypeName;
    }

    public void setRelationshipTypeName(String relationshipTypeName) {
        this.relationshipTypeName = relationshipTypeName;
    }

    public Integer getRelationshipTierTypeID() {
        return relationshipTierTypeID;
    }

    public void setRelationshipTierTypeID(Integer relationshipTierTypeID) {
        this.relationshipTierTypeID = relationshipTierTypeID;
    }

    public String getRelationshipTierTypeName() {
        return relationshipTierTypeName;
    }

    public void setRelationshipTierTypeName(String relationshipTierTypeName) {
        this.relationshipTierTypeName = relationshipTierTypeName;
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

    public Integer getFamilyOfOriginTypeId() {
        return familyOfOriginTypeId;
    }

    public void setFamilyOfOriginTypeId(Integer familyOfOriginTypeId) {
        this.familyOfOriginTypeId = familyOfOriginTypeId;
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
