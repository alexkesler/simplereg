package org.kesler.simplereg.logic.reception;

import javax.persistence.Column;

import org.kesler.simplereg.dao.AbstractEntity;

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