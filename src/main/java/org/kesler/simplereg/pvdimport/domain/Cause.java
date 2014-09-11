package org.kesler.simplereg.pvdimport.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cause {
    private String id;
    private Package pack;
    private String regnum;
    private String procId;
    private List<Applicant> applicants;
    private List<Obj> objects;
    private List<Pay> pays;
    private Date startDate;
    private Date estimateDate;
    private Integer state;
    private String statusMD;
    private Integer purpose;

    public Cause() {
        applicants = new ArrayList<Applicant>();
        objects = new ArrayList<Obj>();
        pays = new ArrayList<Pay>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Package getPackage() { return pack; }
    public void setPackage(Package aPackage) { this.pack = aPackage; }

    public String getRegnum() { return regnum; }
    public void setRegnum(String regnum) { this.regnum = regnum; }

    public String getTypeId() { return pack.getTypeId(); }

    public String getGroupType() { return pack.getGroupType(); }

    public String getType() { return pack.getType(); }

    public String getProcId() { return procId; }
    public void setProcId(String procId) { this.procId = procId; }

    public List<Applicant> getApplicants() {return applicants;}

    public List<Obj> getObjects() { return objects; }

    public List<Pay> getPays() {return pays;}

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEstimateDate() { return estimateDate; }
    public void setEstimateDate(Date estimateDate) { this.estimateDate = estimateDate; }

    public Integer getState() { return state; }
    public void setState(Integer state) { this.state = state; }

    public String getStatusMD() {  return statusMD; }
    public void setStatusMD(String statusMD) { this.statusMD = statusMD; }

    public Integer getPurpose() { return purpose; }
    public void setPurpose(Integer purpose) { this.purpose = purpose; }

    public Date getBeginDate() { return pack.getBeginDate(); }

    public Integer getPackageNum() { return pack.getNum(); }

    public Double getTotalCharge() {
        Double totalCharge = 0.0;
        for(Pay pay:pays) {
            totalCharge += pay.getCharge();
        }

        return totalCharge;
    }
}
