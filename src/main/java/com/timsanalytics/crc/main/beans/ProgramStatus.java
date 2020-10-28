package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramStatus {
    private Integer programStatusId;
    private String programStatusName;
    private Boolean hasChildren;

    public Integer getProgramStatusId() {
        return programStatusId;
    }

    public void setProgramStatusId(Integer programStatusId) {
        this.programStatusId = programStatusId;
    }

    public String getProgramStatusName() {
        return programStatusName;
    }

    public void setProgramStatusName(String programStatusName) {
        this.programStatusName = programStatusName;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
