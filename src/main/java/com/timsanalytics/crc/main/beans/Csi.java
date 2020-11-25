package com.timsanalytics.crc.main.beans;

public class Csi {
    private Integer csiId;
    private Integer studentId;
    private Integer caseManagerId;
    private String csiDate;
    private String csiServicesProvided;
    private String csiComments;
    private Integer csiScoreFoodSecurity;
    private Integer csiScoreNutritionAndGrowth;
    private Integer csiScoreShelter;
    private Integer csiScoreCare;
    private Integer csiScoreAbuseAndExploitation;
    private Integer csiScoreLegalProtection;
    private Integer csiScoreWellness;
    private Integer csiScoreEmotionalHealth;
    private Integer csiScoreSocialBehavior;
    private Integer csiScorePerformance;
    private Integer csiScoreEducationAndWork;
    // JOINED TABLES
    private String studentSurname;
    private String studentGivenName;
    private String caseManagerSurname;
    private String caseManagerGivenName;

    public Integer getCsiId() {
        return csiId;
    }

    public void setCsiId(Integer csiId) {
        this.csiId = csiId;
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

    public String getCsiDate() {
        return csiDate;
    }

    public void setCsiDate(String csiDate) {
        this.csiDate = csiDate;
    }

    public String getCsiServicesProvided() {
        return csiServicesProvided;
    }

    public void setCsiServicesProvided(String csiServicesProvided) {
        this.csiServicesProvided = csiServicesProvided;
    }

    public String getCsiComments() {
        return csiComments;
    }

    public void setCsiComments(String csiComments) {
        this.csiComments = csiComments;
    }

    public Integer getCsiScoreFoodSecurity() {
        return csiScoreFoodSecurity;
    }

    public void setCsiScoreFoodSecurity(Integer csiScoreFoodSecurity) {
        this.csiScoreFoodSecurity = csiScoreFoodSecurity;
    }

    public Integer getCsiScoreNutritionAndGrowth() {
        return csiScoreNutritionAndGrowth;
    }

    public void setCsiScoreNutritionAndGrowth(Integer csiScoreNutritionAndGrowth) {
        this.csiScoreNutritionAndGrowth = csiScoreNutritionAndGrowth;
    }

    public Integer getCsiScoreShelter() {
        return csiScoreShelter;
    }

    public void setCsiScoreShelter(Integer csiScoreShelter) {
        this.csiScoreShelter = csiScoreShelter;
    }

    public Integer getCsiScoreCare() {
        return csiScoreCare;
    }

    public void setCsiScoreCare(Integer csiScoreCare) {
        this.csiScoreCare = csiScoreCare;
    }

    public Integer getCsiScoreAbuseAndExploitation() {
        return csiScoreAbuseAndExploitation;
    }

    public void setCsiScoreAbuseAndExploitation(Integer csiScoreAbuseAndExploitation) {
        this.csiScoreAbuseAndExploitation = csiScoreAbuseAndExploitation;
    }

    public Integer getCsiScoreLegalProtection() {
        return csiScoreLegalProtection;
    }

    public void setCsiScoreLegalProtection(Integer csiScoreLegalProtection) {
        this.csiScoreLegalProtection = csiScoreLegalProtection;
    }

    public Integer getCsiScoreWellness() {
        return csiScoreWellness;
    }

    public void setCsiScoreWellness(Integer csiScoreWellness) {
        this.csiScoreWellness = csiScoreWellness;
    }

    public Integer getCsiScoreEmotionalHealth() {
        return csiScoreEmotionalHealth;
    }

    public void setCsiScoreEmotionalHealth(Integer csiScoreEmotionalHealth) {
        this.csiScoreEmotionalHealth = csiScoreEmotionalHealth;
    }

    public Integer getCsiScoreSocialBehavior() {
        return csiScoreSocialBehavior;
    }

    public void setCsiScoreSocialBehavior(Integer csiScoreSocialBehavior) {
        this.csiScoreSocialBehavior = csiScoreSocialBehavior;
    }

    public Integer getCsiScorePerformance() {
        return csiScorePerformance;
    }

    public void setCsiScorePerformance(Integer csiScorePerformance) {
        this.csiScorePerformance = csiScorePerformance;
    }

    public Integer getCsiScoreEducationAndWork() {
        return csiScoreEducationAndWork;
    }

    public void setCsiScoreEducationAndWork(Integer csiScoreEducationAndWork) {
        this.csiScoreEducationAndWork = csiScoreEducationAndWork;
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
}
