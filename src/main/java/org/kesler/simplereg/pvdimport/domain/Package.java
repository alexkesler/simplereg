package org.kesler.simplereg.pvdimport.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Package {
    private String id;
    private String regnum;
    private Integer num;
    private String typeId;
    private String groupType;
    private String type;
    private Date beginDate;
    private List<Cause> causes;

    public Package() {
        causes = new ArrayList<Cause>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRegnum() { return regnum; }
    public void setRegnum(String regnum) { this.regnum = regnum; }

    public Integer getNum() { return num; }
    public void setNum(Integer num) { this.num = num; }

    public String getTypeId() { return typeId; }
    public void setTypeId(String typeId) { this.typeId = typeId; }

    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Date getBeginDate() { return beginDate; }
    public void setBeginDate(Date beginDate) { this.beginDate = beginDate; }

    public List<Cause> getCauses() {return causes;}

}
