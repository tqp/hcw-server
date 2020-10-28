package com.timsanalytics.crc.main.beans;

public class Loan {
    private Integer loanId;
    private Integer caregiverId;
    private String caregiverSurname;
    private String caregiverGivenName;
    private String loanDescription;
    private Double loanAmount;
    private Double amountPaid;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(Integer caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getCaregiverSurname() {
        return caregiverSurname;
    }

    public void setCaregiverSurname(String caregiverSurname) {
        this.caregiverSurname = caregiverSurname;
    }

    public String getCaregiverGivenName() {
        return caregiverGivenName;
    }

    public void setCaregiverGivenName(String caregiverGivenName) {
        this.caregiverGivenName = caregiverGivenName;
    }

    public String getLoanDescription() {
        return loanDescription;
    }

    public void setLoanDescription(String loanDescription) {
        this.loanDescription = loanDescription;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
}
