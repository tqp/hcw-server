package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostGradEventType {
    private Integer postGradEventTypeId;
    private String postGradEventTypeName;

    public Integer getPostGradEventTypeId() {
        return postGradEventTypeId;
    }

    public void setPostGradEventTypeId(Integer postGradEventTypeId) {
        this.postGradEventTypeId = postGradEventTypeId;
    }

    public String getPostGradEventTypeName() {
        return postGradEventTypeName;
    }

    public void setPostGradEventTypeName(String postGradEventTypeName) {
        this.postGradEventTypeName = postGradEventTypeName;
    }
}
