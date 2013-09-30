package org.kesler.simplereg.logic.operator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="Operators")
public class Operator extends AbstractEntity{

	@Column(name="FirstName")
	private String firstName;

	@Column(name="ParentName")
	private String parentName;

	@Column(name="SurName")
	private String surName;

	@Column(name="Password", length=50)
	private String password;

	@Column(name="IsControler")
	private Boolean isControler;

	@Column(name="IsAdmin")
	private Boolean isAdmin;

	@Column(name="Enabled")
	private Boolean enabled;

	public Operator() {
		// for Hibernate
	}

	public String getFIO() {
		return getSurName() + " " + getFirstName() + " " + getParentName();
	}

	public String getShortFIO() {
		String firstNameShort = "";
		if (!getFirstName().isEmpty()) firstNameShort = getFirstName().substring(0,1);
		String parentNameShort = "";
		if(!getParentName().isEmpty()) parentNameShort = getParentName().substring(0,1);
		String fioShort = getSurName() + " " +
							(firstNameShort.isEmpty()?"":firstNameShort + ".") + 
							(parentNameShort.isEmpty()?"":parentNameShort + ".");
		return fioShort; 
	}

	public String getFirstName() {
		String notNullFirstName = "";
		if (firstName != null) notNullFirstName = firstName;
		return notNullFirstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getParentName() {
		String notNullParentName = "";
		if (parentName != null) notNullParentName = parentName;
		return notNullParentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	} 

	public String getSurName() {
		String notNullSurName = "";
		if (surName != null) notNullSurName = surName;
		return notNullSurName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsControler() {
		return isControler;
	}

	public void setIsControler(Boolean isControler) {
		this.isControler = isControler;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override 
	public String toString() {
		return getFIO();
	}
}