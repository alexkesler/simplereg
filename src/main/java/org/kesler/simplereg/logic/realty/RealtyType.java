package org.kesler.simplereg.logic.realty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Table(name="RealtyType")
public class RealtyType extends AbstractEntity {

	@Column(name="Name", length=50)
	private String name;

	public RealtyType() {} // for Hibernate

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}