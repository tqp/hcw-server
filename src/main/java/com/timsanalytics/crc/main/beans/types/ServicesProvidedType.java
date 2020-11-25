package com.timsanalytics.crc.main.beans.types;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServicesProvidedType {
    private Integer servicesProvidedTypeId;
    private String servicesProvidedTypeName;

    public Integer getServicesProvidedTypeId() {
        return servicesProvidedTypeId;
    }

    public void setServicesProvidedTypeId(Integer servicesProvidedTypeId) {
        this.servicesProvidedTypeId = servicesProvidedTypeId;
    }

    public String getServicesProvidedTypeName() {
        return servicesProvidedTypeName;
    }

    public void setServicesProvidedTypeName(String servicesProvidedTypeName) {
        this.servicesProvidedTypeName = servicesProvidedTypeName;
    }
}
