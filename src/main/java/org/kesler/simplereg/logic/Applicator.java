package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="Applicators")
public class Applicator extends AbstractEntity{

	@Column(name="FirstName", length=45)
	private String firstName = "";

	@Column(name="ParentName", length=45)
	private String parentName = "";

	@Column(name="SurName", length=45)
	private String surName = "";

	public Applicator() {
		// for Hibernate
	}

	public Applicator(String firstName, String parentName, String surName) {
		this.firstName = firstName;
		this.parentName = parentName;
		this.surName = surName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getFIO() {
		String fio = surName + " " + firstName + " " + parentName;
		return fio;
	}

	public String getFIOShort() {
		
		String firstNameShort = firstName.isEmpty()?"":firstName.substring(0,1);
		String parentNameShort = parentName.isEmpty()?"":parentName.substring(0,1);

		String fioShort = surName + " " + firstNameShort + "." + parentNameShort + ".";
		return fioShort; 
	}

}