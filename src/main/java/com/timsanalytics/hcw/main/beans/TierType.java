package com.timsanalytics.hcw.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TierType {
    private String tierTypeGuid;
    private String tierTypeName;

    public String getTierTypeGuid() {
        return tierTypeGuid;
    }

    public void setTierTypeGuid(String tierTypeGuid) {
        this.tierTypeGuid = tierTypeGuid;
    }

    public String getTierTypeName() {
        return tierTypeName;
    }

    public void setTierTypeName(String tierTypeName) {
        this.tierTypeName = tierTypeName;
    }
}
