package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sponsor {
    private int sponsorId;
    private String sponsorSurname;
    private String sponsorGivenName;
    private String sponsorAddress;
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

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorSurname() {
        return sponsorSurname;
    }

    public void setSponsorSurname(String sponsorSurname) {
        this.sponsorSurname = sponsorSurname;
    }

    public String getSponsorGivenName() {
        return sponsorGivenName;
    }

    public void setSponsorGivenName(String sponsorGivenName) {
        this.sponsorGivenName = sponsorGivenName;
    }

    public String getSponsorAddress() {
        return sponsorAddress;
    }

    public void setSponsorAddress(String sponsorAddress) {
        this.sponsorAddress = sponsorAddress;
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
