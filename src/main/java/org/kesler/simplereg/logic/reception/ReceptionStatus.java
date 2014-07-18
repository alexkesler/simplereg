package org.kesler.simplereg.logic.reception;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Table(name="ReceptionStatuses")
public class ReceptionStatus extends AbstractEntity {
	
	@Column(name="Name", length=50, nullable=false)
	private String name;

	@Column(name="Code", nullable=false, unique=true)
	private Integer code;

    @Column(name="initial")
    private Boolean initial;

    @Column(name="closed")
    private Boolean closed;

	public ReceptionStatus() {}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}

    public Boolean getInitial() {
        return initial;
    }
    public void setInitial(Boolean initial) {
        this.initial = initial;
    }

    public Boolean getClosed() {
        return closed;
    }
    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
	public String toString() {
		return getCode() + " - " + getName(); 
	}


}