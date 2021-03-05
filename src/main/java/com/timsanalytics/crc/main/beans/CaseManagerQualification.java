package com.timsanalytics.crc.main.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaseManagerQualification {
    private Integer caseManagerQualificationId;
    private Integer caseManagerId;
    private String qualificationInstitution;
    private String qualificationName;

    public Integer getCaseManagerQualificationId() {
        return caseManagerQualificationId;
    }

    public void setCaseManagerQualificationId(Integer caseManagerQualificationId) {
        this.caseManagerQualificationId = caseManagerQualificationId;
    }

    public Integer getCaseManagerId() {
        return caseManagerId;
    }

    public void setCaseManagerId(Integer caseManagerId) {
        this.caseManagerId = caseManagerId;
    }

    public String getQualificationInstitution() {
        return qualificationInstitution;
    }

    public void setQualificationInstitution(String qualificationInstitution) {
        this.qualificationInstitution = qualificationInstitution;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
}
