package org.kesler.simplereg.logic.applicator;

import 

import org.kesler.simplereg.dao.AbstractEntity;


public class UL extends AbstractEntity {
	private String fullName;
	private String shortName;

	public UL () {}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}