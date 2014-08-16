package org.kesler.simplereg.pvdimport.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cause {
    private String id;
    private String regnum;
    private String groupType;
    private String type;
    private List<Applicant> applicants;
    private Date estimateDate;

    public Cause() {
        applicants = new ArrayList<Applicant>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegnum() {
        return regnum;
    }

    public void setRegnum(String regnum) {
        this.regnum = regnum;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Applicant> getApplicants() {return applicants;}

    public Date getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(Date estimateDate) {
        this.estimateDate = estimateDate;
    }
}
