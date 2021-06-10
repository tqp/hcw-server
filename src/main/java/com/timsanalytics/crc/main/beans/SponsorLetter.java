package com.timsanalytics.crc.main.beans;

public class SponsorLetter {
    private Integer sponsorLetterId;
    private Integer studentId;
    private Integer sponsorId;
    private String sponsorLetterDate;
    // JOINED TABLES
    private String studentSurname;
    private String studentGivenName;
    private String sponsorSurname;
    private String sponsorGivenName;

    public Integer getSponsorLetterId() {
        return sponsorLetterId;
    }

    public void setSponsorLetterId(Integer sponsorLetterId) {
        this.sponsorLetterId = sponsorLetterId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(Integer sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsorLetterDate() {
        return sponsorLetterDate;
    }

    public void setSponsorLetterDate(String sponsorLetterDate) {
        this.sponsorLetterDate = sponsorLetterDate;
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
