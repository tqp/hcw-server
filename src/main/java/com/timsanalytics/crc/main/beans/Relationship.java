package com.timsanalytics.crc.main.beans;

public class Relationship {
    private Integer studentId;
    private Integer relationshipId;
    private Integer relationshipEntityId;
    private String relationshipType;
    private String relationshipStartDate;
    private Integer relationshipTierTypeId;
    private Integer relationshipTypeId;
    private Integer relationshipFamilyOfOriginTypeId;
    private Integer relationshipBloodRelative;
    // Joined Tables
    private String studentSurname;
    private String studentGivenName;
    private String relationshipTierTypeName;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
    }

    public Integer getRelationshipEntityId() {
        return relationshipEntityId;
    }

    public void setRelationshipEntityId(Integer relationshipEntityId) {
        this.relationshipEntityId = relationshipEntityId;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public String getRelationshipStartDate() {
        return relationshipStartDate;
    }

    public void setRelationshipStartDate(String relationshipStartDate) {
        this.relationshipStartDate = relationshipStartDate;
    }

    public Integer getRelationshipTierTypeId() {
        return relationshipTierTypeId;
    }

    public void setRelationshipTierTypeId(Integer relationshipTierTypeId) {
        this.relationshipTierTypeId = relationshipTierTypeId;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
    }

    public Integer getRelationshipFamilyOfOriginTypeId() {
        return relationshipFamilyOfOriginTypeId;
    }

    public void setRelationshipFamilyOfOriginTypeId(Integer relationshipFamilyOfOriginTypeId) {
        this.relationshipFamilyOfOriginTypeId = relationshipFamilyOfOriginTypeId;
    }

    public Integer getRelationshipBloodRelative() {
        return relationshipBloodRelative;
    }

    public void setRelationshipBloodRelative(Integer relationshipBloodRelative) {
        this.relationshipBloodRelative = relationshipBloodRelative;
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

    public String getRelationshipTierTypeName() {
        return relationshipTierTypeName;
    }

    public void setRelationshipTierTypeName(String relationshipTierTypeName) {
        this.relationshipTierTypeName = relationshipTierTypeName;
    }
}
