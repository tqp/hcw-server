package com.timsanalytics.crc.main.beans.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImpairmentType {
    private Integer impairmentTypeId;
    private String impairmentTypeName;

    public Integer getImpairmentTypeId() {
        return impairmentTypeId;
    }

    public void setImpairmentTypeId(Integer impairmentTypeId) {
        this.impairmentTypeId = impairmentTypeId;
    }

    public String getImpairmentTypeName() {
        return impairmentTypeName;
    }

    public void setImpairmentTypeName(String impairmentTypeName) {
        this.impairmentTypeName = impairmentTypeName;
    }
}
