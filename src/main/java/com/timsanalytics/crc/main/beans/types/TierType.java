package com.timsanalytics.crc.main.beans.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TierType {
    private Integer tierTypeId;
    private String tierTypeName;

    public Integer getTierTypeId() {
        return tierTypeId;
    }

    public void setTierTypeId(Integer tierTypeId) {
        this.tierTypeId = tierTypeId;
    }

    public String getTierTypeName() {
        return tierTypeName;
    }

    public void setTierTypeName(String tierTypeName) {
        this.tierTypeName = tierTypeName;
    }
}
