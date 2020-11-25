package com.timsanalytics.crc.main.beans.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractionType {
    private Integer interactionTypeId;
    private String interactionTypeName;

    public Integer getInteractionTypeId() {
        return interactionTypeId;
    }

    public void setInteractionTypeId(Integer interactionTypeId) {
        this.interactionTypeId = interactionTypeId;
    }

    public String getInteractionTypeName() {
        return interactionTypeName;
    }

    public void setInteractionTypeName(String interactionTypeName) {
        this.interactionTypeName = interactionTypeName;
    }
}
