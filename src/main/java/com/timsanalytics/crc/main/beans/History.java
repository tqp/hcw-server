package com.timsanalytics.crc.main.beans;

public class History {
    private Integer historyTypeId;
    private String historyTypeName;
    private Integer entityId;
    private Integer deletedStatus;
    private String startDate;
    private String historyDescription;

    public Integer getHistoryTypeId() {
        return historyTypeId;
    }

    public void setHistoryTypeId(Integer historyTypeId) {
        this.historyTypeId = historyTypeId;
    }

    public String getHistoryTypeName() {
        return historyTypeName;
    }

    public void setHistoryTypeName(String historyTypeName) {
        this.historyTypeName = historyTypeName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(Integer deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getHistoryDescription() {
        return historyDescription;
    }

    public void setHistoryDescription(String historyDescription) {
        this.historyDescription = historyDescription;
    }
}
