package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workshop {
    private Integer caregiverWorkshopId;
    private String workshopName;
    private Date workshopDate;

    public Integer getCaregiverWorkshopId() {
        return caregiverWorkshopId;
    }

    public void setCaregiverWorkshopId(Integer caregiverWorkshopId) {
        this.caregiverWorkshopId = caregiverWorkshopId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public Date getWorkshopDate() {
        return workshopDate;
    }

    public void setWorkshopDate(Date workshopDate) {
        this.workshopDate = workshopDate;
    }
}
