package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Relation {
    private Integer relationId;
    private String relationSurname;
    private String relationGivenName;
    private String relationGender;
    // Joined Tables
    private String relationshipType;
    private Boolean relationshipBloodRelative;
    // Metadata
    private String status;
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationSurname() {
        return relationSurname;
    }

    public void setRelationSurname(String relationSurname) {
        this.relationSurname = relationSurname;
    }

    public String getRelationGivenName() {
        return relationGivenName;
    }

    public void setRelationGivenName(String relationGivenName) {
        this.relationGivenName = relationGivenName;
    }

    public String getRelationGender() {
        return relationGender;
    }

    public void setRelationGender(String relationGender) {
        this.relationGender = relationGender;
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
