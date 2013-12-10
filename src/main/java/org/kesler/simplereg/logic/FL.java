package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

/**
* Класс для физического лица
*/
@Entity
@Table(name="FL")
@Proxy(lazy=false)
public class FL extends AbstractEntity{
	
	@Column (name = "FirstName")
	private String firstName;

	@Column (name = "ParentName")
	private String parentName;

	@Column (name = "SurName")
	private String surName;

	public FL () {}

	public String getFirstName() {
		String notNullFirstName = "";
		if (firstName!=null) notNullFirstName = firstName;
		return notNullFirstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getParentName() {
		String notNullParentName = "";
		if(parentName!=null) notNullParentName = parentName;
		return notNullParentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getSurName() {
		String notNullSurname = "";
		if(surName!=null) notNullSurname = surName;
		return notNullSurname;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	/**
	* Возвращает фамилию и инициалы
	*/
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

	public String getFIO() {
		String fio = "";

		fio = getSurName() + " " + getFirstName() + " " + getParentName();

		return fio;
	}

	@Override
	public String toString() {
		return getFIO();
	}


}