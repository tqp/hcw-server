package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramStatus {
    private Integer studentId;
    private Integer programStatusId;
    private Integer programStatusLevelOneId;
    private String programStatusLevelOneName;
    private Integer programStatusLevelTwoId;
    private String programStatusLevelTwoName;
    private String programStatusStartDate;
    // Metadata
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;
    private String deleted;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getProgramStatusId() {
        return programStatusId;
    }

    public void setProgramStatusId(Integer programStatusId) {
        this.programStatusId = programStatusId;
    }

    public Integer getProgramStatusLevelOneId() {
        return programStatusLevelOneId;
    }

    public void setProgramStatusLevelOneId(Integer programStatusLevelOneId) {
        this.programStatusLevelOneId = programStatusLevelOneId;
    }

    public String getProgramStatusLevelOneName() {
        return programStatusLevelOneName;
    }

    public void setProgramStatusLevelOneName(String programStatusLevelOneName) {
        this.programStatusLevelOneName = programStatusLevelOneName;
    }

    public Integer getProgramStatusLevelTwoId() {
        return programStatusLevelTwoId;
    }

    public void setProgramStatusLevelTwoId(Integer programStatusLevelTwoId) {
        this.programStatusLevelTwoId = programStatusLevelTwoId;
    }

    public String getProgramStatusLevelTwoName() {
        return programStatusLevelTwoName;
    }

    public void setProgramStatusLevelTwoName(String programStatusLevelTwoName) {
        this.programStatusLevelTwoName = programStatusLevelTwoName;
    }

    public String getProgramStatusStartDate() {
        return programStatusStartDate;
    }

    public void setProgramStatusStartDate(String programStatusStartDate) {
        this.programStatusStartDate = programStatusStartDate;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
