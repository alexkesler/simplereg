package org.kesler.simplereg.logic.applicator;

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

	/**
	* Возвращает фамилию и инициалы
	*/
	public String getShortFIO() {
		String firstNameShort = firstName.isEmpty()?"":firstName.substring(0,1);
		String parentNameShort = parentName.isEmpty()?"":parentName.substring(0,1);
		String fioShort = surName + " " + firstNameShort + "." + parentNameShort + ".";
		return fioShort; 
	}


}