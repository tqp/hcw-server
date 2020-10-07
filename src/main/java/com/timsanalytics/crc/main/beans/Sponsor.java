package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sponsor {
    private int sponsorId;
    private String sponsorSurname;
    private String sponsorGivenName;
    private String sponsorGender;
    // Joined Tables
    private String relationshipEffectiveDate;
    private String relationshipType;
    // Metadata
    private String status;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;

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

    public String getSponsorGender() {
        return sponsorGender;
    }

    public void setSponsorGender(String sponsorGender) {
        this.sponsorGender = sponsorGender;
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
