package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseManager {
    private Integer caseManagerUserId;
    private Integer caseManagerId;
    private String caseManagerSurname;
    private String caseManagerGivenName;
    private String caseManagerGender;
    private String caseManagerAddress;
    private String caseManagerPhone;
    private String caseManagerEmail;
    // Joined Tables
    private Integer relationshipId;
    private String relationshipStartDate;
    private String relationshipEndDate;
    private Integer studentCount;
    // Metadata
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;
    private String deleted;

    public Integer getCaseManagerUserId() {
        return caseManagerUserId;
    }

    public void setCaseManagerUserId(Integer caseManagerUserId) {
        this.caseManagerUserId = caseManagerUserId;
    }

    public Integer getCaseManagerId() {
        return caseManagerId;
    }

    public void setCaseManagerId(Integer caseManagerId) {
        this.caseManagerId = caseManagerId;
    }

    public String getCaseManagerSurname() {
        return caseManagerSurname;
    }

    public void setCaseManagerSurname(String caseManagerSurname) {
        this.caseManagerSurname = caseManagerSurname;
    }

    public String getCaseManagerGivenName() {
        return caseManagerGivenName;
    }

    public void setCaseManagerGivenName(String caseManagerGivenName) {
        this.caseManagerGivenName = caseManagerGivenName;
    }

    public String getCaseManagerGender() {
        return caseManagerGender;
    }

    public void setCaseManagerGender(String caseManagerGender) {
        this.caseManagerGender = caseManagerGender;
    }

    public String getCaseManagerAddress() {
        return caseManagerAddress;
    }

    public void setCaseManagerAddress(String caseManagerAddress) {
        this.caseManagerAddress = caseManagerAddress;
    }

    public String getCaseManagerPhone() {
        return caseManagerPhone;
    }

    public void setCaseManagerPhone(String caseManagerPhone) {
        this.caseManagerPhone = caseManagerPhone;
    }

    public String getCaseManagerEmail() {
        return caseManagerEmail;
    }

    public void setCaseManagerEmail(String caseManagerEmail) {
        this.caseManagerEmail = caseManagerEmail;
    }

    public Integer getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
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

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
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
