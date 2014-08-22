package org.kesler.simplereg.pvdimport.domain;

public class Obj {
    private String id;
    private Cause cause;
    private Package pack;
    private String fullAddress;
    private Integer objtype;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Cause getCause() { return cause; }
    public void setCause(Cause cause) { this.cause = cause; }

    public Package getPackage() { return pack; }
    public void setPackage(Package pack) { this.pack = pack; }

    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

    public Integer getObjtype() { return objtype; }
    public void setObjtype(Integer objtype) { this.objtype = objtype; }
}
