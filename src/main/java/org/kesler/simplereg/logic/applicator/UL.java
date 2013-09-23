package org.kesler.simplereg.logic.applicator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

/**
* Класс для юридического лица
*/
@Entity
@Table(name="UL")
@Proxy(lazy=false)
public class UL extends AbstractEntity {
	
	@Column(name="FullName")
	private String fullName;

	@Column(name="ShortName")
	private String shortName;

	public UL () {}

	public String getFullName() {
		String notNullFullName = "";
		if (fullName!=null) notNullFullName = fullName;
		return notNullFullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		String notNullShortName = "";
		if(shortName!=null) notNullShortName = shortName;
		return notNullShortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return getShortName();
	}
}