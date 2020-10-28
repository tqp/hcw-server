package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseManager {
    private int caseManagerId;
    private String caseManagerSurname;
    private String caseManagerGivenName;
    private String caseManagerGender;
    private String caseManagerPhone;
    private String caseManagerEmail;
    // Joined Tables
    private String relationshipStartDate;
    private String relationshipEndDate;
    private Integer studentCount;
    // Metadata
    private String status;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;

    public int getCaseManagerId() {
        return caseManagerId;
    }

    public void setCaseManagerId(int caseManagerId) {
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
