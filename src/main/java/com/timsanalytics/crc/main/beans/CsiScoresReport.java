package com.timsanalytics.crc.main.beans;

public class CsiScoresReport {
    private Integer caseManagerId;
    private String caseManagerSurname;
    private String caseManagerGivenName;
    private Integer totalScore;
    private Integer count;

    public Integer getCaseManagerId() {
        return caseManagerId;
    }

    public void setCaseManagerId(Integer caseManagerId) {
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
