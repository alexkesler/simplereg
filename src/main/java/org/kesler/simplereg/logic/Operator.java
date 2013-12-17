package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="Operators")
public class Operator extends AbstractEntity{

	@Column(name="Code", length=25)
	private String code;

	@Column(name="FilialCode", length=25)
	private String filialCode;

	@Column(name="FirstName", length=25)
	private String firstName;

	@Column(name="ParentName", length=25)
	private String parentName;

	@Column(name="SurName", length=25)
	private String surName;

	@Column(name="Password", length=50)
	private String password;

	@Column(name="IsControler")
	private Boolean controler;

	@Column(name="IsAdmin")
	private Boolean admin;

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

	public String getCode() {
		String notNullCode = "";
		if (code != null) notNullCode = code;
		return notNullCode;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFilialCode() {
		String notNullFilialCode = "";
		if (filialCode != null) notNullFilialCode = filialCode;
		return notNullFilialCode;
	}

	public void setFilialCode(String filialCode) {
		this.filialCode = filialCode;
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

	public Boolean isControler() {
		return controler;
	}

	public void setControler(Boolean controler) {
		this.controler = controler;
	}

	public Boolean isAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override 
	public String toString() {
		

		String controlerString = "";
		if (controler) {
			controlerString = "Контр;";
		}

		String adminString = "";
		if (admin) {
			adminString = "Админ;";
		}

		String enabledString = "";
		if (enabled) {
			enabledString = "Действ;";
		} 

		return getFIO() + " [" + getCode() + "]"+ " (" + controlerString + adminString + enabledString + ")";
	}
}