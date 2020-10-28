package com.timsanalytics.crc.main.beans;

public class StudentRelationship {
    private Integer studentId;
    private String relationshipType;
    private Integer relationshipPersonId;
    private String relationshipStartDate;
    private Integer relationshipTierTypeId;
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

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Integer getRelationshipPersonId() {
        return relationshipPersonId;
    }

    public void setRelationshipPersonId(Integer relationshipPersonId) {
        this.relationshipPersonId = relationshipPersonId;
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
