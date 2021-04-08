package com.timsanalytics.crc.main.beans;

public class StudentSponsorLetter {
    private Integer studentSponsorLetterId;
    private Integer studentId;
    private String sponsorId;
    private String studentSponsorLetterDate;
    // JOINED TABLES
    private String studentSurname;
    private String studentGivenName;
    private String sponsorSurname;
    private String sponsorGivenName;

    public Integer getStudentSponsorLetterId() {
        return studentSponsorLetterId;
    }

    public void setStudentSponsorLetterId(Integer studentSponsorLetterId) {
        this.studentSponsorLetterId = studentSponsorLetterId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getStudentSponsorLetterDate() {
        return studentSponsorLetterDate;
    }

    public void setStudentSponsorLetterDate(String studentSponsorLetterDate) {
        this.studentSponsorLetterDate = studentSponsorLetterDate;
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
}
