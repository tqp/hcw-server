package com.timsanalytics.crc.main.beans.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitType {
    private Integer VisitTypeId;
    private String VisitTypeName;

    public Integer getVisitTypeId() {
        return VisitTypeId;
    }

    public void setVisitTypeId(Integer VisitTypeId) {
        this.VisitTypeId = VisitTypeId;
    }

    public String getVisitTypeName() {
        return VisitTypeName;
    }

    public void setVisitTypeName(String VisitTypeName) {
        this.VisitTypeName = VisitTypeName;
    }
}
