package org.kesler.simplereg.logic.reception;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="ReceptionStatuses")
public class ReceptionStatus extends AbstractEntity {
	
	@Column(name="Name", length=50, nullable=false)
	private String name;

	@Column(name="Code", nullable=false, unique=true)
	private Integer code;


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

	@Override
	public String toString() {
		return getCode() + " - " + getName(); 
	}


}