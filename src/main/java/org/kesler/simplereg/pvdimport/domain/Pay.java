package org.kesler.simplereg.pvdimport.domain;

public class Pay {
    private String id;
    private String causeId;
    private String applicantId;
    private Double charge;
    private String paycode;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getCauseId() { return causeId; }

    public void setCauseId(String causeId) { this.causeId = causeId; }

    public String getApplicantId() { return applicantId; }

    public void setApplicantId(String applicantId) { this.applicantId = applicantId; }

    public Double getCharge() { return charge; }

    public void setCharge(Double charge) { this.charge = charge; }

    public String getPaycode() { return paycode; }

    public void setPaycode(String paycode) { this.paycode = paycode; }
}
