package com.timsanalytics.crc.main.beans;

public class Visit {
    private Integer visitId;
    private Integer studentId;
    private Integer caseManagerUserId;
    private String visitDate;
    private Integer visitTypeId;
    private Integer interactionTypeId;
    private String caregiverComments;
    private String caseManagerComments;
    // Joined Tables
    private String studentSurname;
    private String studentGivenName;
    private String caseManagerSurname;
    private String caseManagerGivenName;
    private String visitTypeName;
    private String interactionTypeName;

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCaseManagerUserId() {
        return caseManagerUserId;
    }

    public void setCaseManagerUserId(Integer caseManagerUserId) {
        this.caseManagerUserId = caseManagerUserId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public Integer getVisitTypeId() {
        return visitTypeId;
    }

    public void setVisitTypeId(Integer visitTypeId) {
        this.visitTypeId = visitTypeId;
    }

    public Integer getInteractionTypeId() {
        return interactionTypeId;
    }

    public void setInteractionTypeId(Integer interactionTypeId) {
        this.interactionTypeId = interactionTypeId;
    }

    public String getCaregiverComments() {
        return caregiverComments;
    }

    public void setCaregiverComments(String caregiverComments) {
        this.caregiverComments = caregiverComments;
    }

    public String getCaseManagerComments() {
        return caseManagerComments;
    }

    public void setCaseManagerComments(String caseManagerComments) {
        this.caseManagerComments = caseManagerComments;
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

    public String getVisitTypeName() {
        return visitTypeName;
    }

    public void setVisitTypeName(String visitTypeName) {
        this.visitTypeName = visitTypeName;
    }

    public String getInteractionTypeName() {
        return interactionTypeName;
    }

    public void setInteractionTypeName(String interactionTypeName) {
        this.interactionTypeName = interactionTypeName;
    }
}
