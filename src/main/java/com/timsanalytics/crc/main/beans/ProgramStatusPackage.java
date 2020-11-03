package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramStatusPackage {
    private Integer programStatusId;
    private String programStatusName;
    private String childFieldTitle;
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

    public String getChildFieldTitle() {
        return childFieldTitle;
    }

    public void setChildFieldTitle(String childFieldTitle) {
        this.childFieldTitle = childFieldTitle;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
