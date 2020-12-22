package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostGradEvent {
    private Integer postGradEventId;
    private Integer studentId;
    private Integer postGradEventTypeId;
    private String postGradEventDate;
    private String postGradEventComments;
    // JOINED TABLES
    private String postGradEventTypeName;
    private String studentSurname;
    private String studentGivenName;

    public Integer getPostGradEventId() {
        return postGradEventId;
    }

    public void setPostGradEventId(Integer postGradEventId) {
        this.postGradEventId = postGradEventId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getPostGradEventTypeId() {
        return postGradEventTypeId;
    }

    public void setPostGradEventTypeId(Integer postGradEventTypeId) {
        this.postGradEventTypeId = postGradEventTypeId;
    }

    public String getPostGradEventDate() {
        return postGradEventDate;
    }

    public void setPostGradEventDate(String postGradEventDate) {
        this.postGradEventDate = postGradEventDate;
    }

    public String getPostGradEventComments() {
        return postGradEventComments;
    }

    public void setPostGradEventComments(String postGradEventComments) {
        this.postGradEventComments = postGradEventComments;
    }

    public String getPostGradEventTypeName() {
        return postGradEventTypeName;
    }

    public void setPostGradEventTypeName(String postGradEventTypeName) {
        this.postGradEventTypeName = postGradEventTypeName;
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
}
