package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostGradEvent {
    private Integer postGradEventId;
    private Integer studentId;
    private Integer postGradEventTypeId;
    private String postGradEventDate;
    // JOINED TABLES
    private String postGradEventTypeName;

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

    public String getPostGradEventTypeName() {
        return postGradEventTypeName;
    }

    public void setPostGradEventTypeName(String postGradEventTypeName) {
        this.postGradEventTypeName = postGradEventTypeName;
    }
}
