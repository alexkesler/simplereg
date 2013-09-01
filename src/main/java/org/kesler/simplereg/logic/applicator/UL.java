package org.kesler.simplereg.logic.applicator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Table(name="UL")
public class UL extends AbstractEntity {
	
	@Column(name="FullName")
	private String fullName;

	@Column(name="ShortName")
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