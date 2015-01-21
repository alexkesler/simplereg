package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.dao.EntityState;


@Entity
@Proxy(lazy=false)
@Table(name="Services")
public class Service extends AbstractEntity {

	@ManyToOne
	@JoinColumn(name="parentID")
	private Service parentService;

	@Column(name="Name", length=255)
	private String name;

    @Column(name="Code", length=50)
    private String code;

    @Column(name = "PvdtypesPurposes", length = 511)
    private String pvdtypesPurposes;

	@Column(name="Enabled")
	private Boolean enabled;

	@Column(name = "TemplateUUID")
	private String templateUuid;


	public Service() {} // for Hibernate

	public Service (Service parentService, String name, Boolean enabled) {
		this.parentService = parentService;
		this.name = name;
		this.enabled = enabled;
	}

	public Service(String name) {
		this.name = name;
		this.enabled = true;
	}

	public Service getParentService() {
		return parentService;
	}

	public void setParentService(Service parentService) {
		this.parentService = parentService;
		state = EntityState.CHANGED;
	}

	public String getParentServiceName() {
		String name = "Родительская услуга не определена";
		if (parentService != null) {
			name = parentService.getName();
		}
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		state = EntityState.CHANGED;
	}

    public String getCode() { return code; }

    public void setCode(String code) {this.code = code;}

    public String getPvdtypesPurposes() { return pvdtypesPurposes; }
    public void setPvdtypesPurposes(String pvdtypesPurposes) { this.pvdtypesPurposes = pvdtypesPurposes; }

    public boolean fitPvdtypePurpose(String pvdtype, Integer purpose) {
        if (pvdtype==null || purpose==null) return false;
        if (pvdtypesPurposes==null || pvdtypesPurposes.isEmpty()) return false;

        String pvdtypePurpose = pvdtype + ":" + purpose.toString();

        String[] ps = pvdtypesPurposes.split(",");
        for(String p:ps) {
            if(p.equals(pvdtypePurpose)) return true;
        }
        return false;
    }

    public void addPvdtypePurpose(String pvdtype,Integer purpose) {
        if (pvdtype==null || purpose==null) return;

        String pvdtypePurpose = pvdtype + ":" + purpose.toString();

        if (pvdtypesPurposes==null || pvdtypesPurposes.isEmpty())
            pvdtypesPurposes = pvdtypePurpose;
        else
            if (!pvdtypesPurposes.contains(pvdtypePurpose))
                pvdtypesPurposes += "," + pvdtypePurpose;
		cropPvdtypesPurpose(510);
    }

	private void cropPvdtypesPurpose(int length) {
		if (pvdtypesPurposes!=null && pvdtypesPurposes.length()>length) {
			int nextLength = pvdtypesPurposes.indexOf(",");
			if (nextLength<0) {
				return;
			}

			nextLength++;
			while (pvdtypesPurposes.length()-nextLength > length) {
				nextLength = pvdtypesPurposes.indexOf(",",nextLength);
				if (nextLength<0) return;
				nextLength++;
			}
			pvdtypesPurposes = pvdtypesPurposes.substring(nextLength);
		}
	}

    public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		state = EntityState.CHANGED;
	}

	public String getTemplateUuid() { return templateUuid;}
	public void setTemplateUuid(String templateUuid) { this.templateUuid = templateUuid; }

	@Override
	public String toString() {
		return name;
	}


}