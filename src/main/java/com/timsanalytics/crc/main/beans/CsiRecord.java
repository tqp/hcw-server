package com.timsanalytics.crc.main.beans;

public class CsiRecord {
    private Integer csiRecordId;
    private Integer studentId;
    private Integer caseManagerUserId;
    private String csiRecordDate;
    private String csiRecordServicesProvided;
    private String csiRecordComments;
    private Integer csiRecordScoreFoodSecurity;
    private Integer csiRecordScoreNutritionAndGrowth;
    private Integer csiRecordScoreShelter;
    private Integer csiRecordScoreCare;
    private Integer csiRecordScoreAbuseAndExploitation;
    private Integer csiRecordScoreLegalProtection;
    private Integer csiRecordScoreWellness;
    private Integer csiRecordScoreHealthCareServices;
    private Integer csiRecordScoreEmotionalHealth;
    private Integer csiRecordScoreSocialBehavior;
    private Integer csiRecordScorePerformance;
    private Integer csiRecordScoreEducationAndWork;
    // JOINED TABLES
    private String studentSurname;
    private String studentGivenName;
    private String caseManagerSurname;
    private String caseManagerGivenName;

    public Integer getCsiRecordId() {
        return csiRecordId;
    }

    public void setCsiRecordId(Integer csiRecordId) {
        this.csiRecordId = csiRecordId;
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

    public String getCsiRecordDate() {
        return csiRecordDate;
    }

    public void setCsiRecordDate(String csiRecordDate) {
        this.csiRecordDate = csiRecordDate;
    }

    public String getCsiRecordServicesProvided() {
        return csiRecordServicesProvided;
    }

    public void setCsiRecordServicesProvided(String csiRecordServicesProvided) {
        this.csiRecordServicesProvided = csiRecordServicesProvided;
    }

    public String getCsiRecordComments() {
        return csiRecordComments;
    }

    public void setCsiRecordComments(String csiRecordComments) {
        this.csiRecordComments = csiRecordComments;
    }

    public Integer getCsiRecordScoreFoodSecurity() {
        return csiRecordScoreFoodSecurity;
    }

    public void setCsiRecordScoreFoodSecurity(Integer csiRecordScoreFoodSecurity) {
        this.csiRecordScoreFoodSecurity = csiRecordScoreFoodSecurity;
    }

    public Integer getCsiRecordScoreNutritionAndGrowth() {
        return csiRecordScoreNutritionAndGrowth;
    }

    public void setCsiRecordScoreNutritionAndGrowth(Integer csiRecordScoreNutritionAndGrowth) {
        this.csiRecordScoreNutritionAndGrowth = csiRecordScoreNutritionAndGrowth;
    }

    public Integer getCsiRecordScoreShelter() {
        return csiRecordScoreShelter;
    }

    public void setCsiRecordScoreShelter(Integer csiRecordScoreShelter) {
        this.csiRecordScoreShelter = csiRecordScoreShelter;
    }

    public Integer getCsiRecordScoreCare() {
        return csiRecordScoreCare;
    }

    public void setCsiRecordScoreCare(Integer csiRecordScoreCare) {
        this.csiRecordScoreCare = csiRecordScoreCare;
    }

    public Integer getCsiRecordScoreAbuseAndExploitation() {
        return csiRecordScoreAbuseAndExploitation;
    }

    public void setCsiRecordScoreAbuseAndExploitation(Integer csiRecordScoreAbuseAndExploitation) {
        this.csiRecordScoreAbuseAndExploitation = csiRecordScoreAbuseAndExploitation;
    }

    public Integer getCsiRecordScoreLegalProtection() {
        return csiRecordScoreLegalProtection;
    }

    public void setCsiRecordScoreLegalProtection(Integer csiRecordScoreLegalProtection) {
        this.csiRecordScoreLegalProtection = csiRecordScoreLegalProtection;
    }

    public Integer getCsiRecordScoreWellness() {
        return csiRecordScoreWellness;
    }

    public void setCsiRecordScoreWellness(Integer csiRecordScoreWellness) {
        this.csiRecordScoreWellness = csiRecordScoreWellness;
    }

    public Integer getCsiRecordScoreHealthCareServices() {
        return csiRecordScoreHealthCareServices;
    }

    public void setCsiRecordScoreHealthCareServices(Integer csiRecordScoreHealthCareServices) {
        this.csiRecordScoreHealthCareServices = csiRecordScoreHealthCareServices;
    }

    public Integer getCsiRecordScoreEmotionalHealth() {
        return csiRecordScoreEmotionalHealth;
    }

    public void setCsiRecordScoreEmotionalHealth(Integer csiRecordScoreEmotionalHealth) {
        this.csiRecordScoreEmotionalHealth = csiRecordScoreEmotionalHealth;
    }

    public Integer getCsiRecordScoreSocialBehavior() {
        return csiRecordScoreSocialBehavior;
    }

    public void setCsiRecordScoreSocialBehavior(Integer csiRecordScoreSocialBehavior) {
        this.csiRecordScoreSocialBehavior = csiRecordScoreSocialBehavior;
    }

    public Integer getCsiRecordScorePerformance() {
        return csiRecordScorePerformance;
    }

    public void setCsiRecordScorePerformance(Integer csiRecordScorePerformance) {
        this.csiRecordScorePerformance = csiRecordScorePerformance;
    }

    public Integer getCsiRecordScoreEducationAndWork() {
        return csiRecordScoreEducationAndWork;
    }

    public void setCsiRecordScoreEducationAndWork(Integer csiRecordScoreEducationAndWork) {
        this.csiRecordScoreEducationAndWork = csiRecordScoreEducationAndWork;
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
