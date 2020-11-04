package com.timsanalytics.crc.main.beans;

public class Visit {
    private Integer studentVisitId;
    private Integer studentId;
    private Integer caseManagerId;
    private String visitDate;
    private String caregiverComments;
    private String caseManagerComments;
    // Joined Tables
    private String studentSurname;
    private String studentGivenName;
    private String visitTypeName;
    private String interactionTypeName;

    public Integer getStudentVisitId() {
        return studentVisitId;
    }

    public void setStudentVisitId(Integer studentVisitId) {
        this.studentVisitId = studentVisitId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCaseManagerId() {
        return caseManagerId;
    }

    public void setCaseManagerId(Integer caseManagerId) {
        this.caseManagerId = caseManagerId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
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
