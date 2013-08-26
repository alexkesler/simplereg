package org.kesler.simplereg.logic.applicator;

public class FL {
	private String firstName;
	private String parentName;
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
	public String getFIOShort() {
		String firstNameShort = firstName.isEmpty()?"":firstName.substring(0,1);
		String parentNameShort = parentName.isEmpty()?"":parentName.substring(0,1);
		String fioShort = surName + " " + firstNameShort + "." + parentNameShort + ".";
		return fioShort; 
	}


}