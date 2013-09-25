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
	
	@Column(name="Name", length=50)
	private String name;

	public ReceptionStatus() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


}