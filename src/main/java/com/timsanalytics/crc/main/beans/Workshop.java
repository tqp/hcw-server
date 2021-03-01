package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Workshop {
    private Integer caregiverWorkshopId;
    private Integer caregiverId;
    private String workshopName;
    private String workshopDate;

    public Integer getCaregiverWorkshopId() {
        return caregiverWorkshopId;
    }

    public void setCaregiverWorkshopId(Integer caregiverWorkshopId) {
        this.caregiverWorkshopId = caregiverWorkshopId;
    }

    public Integer getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Integer caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopDate() {
        return workshopDate;
    }

    public void setWorkshopDate(String workshopDate) {
        this.workshopDate = workshopDate;
    }
}
