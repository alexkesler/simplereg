package org.kesler.simplereg.logic;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.kesler.simplereg.logic.realty.RealtyType;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="RealtyObject")
public class RealtyObject extends AbstractEntity {

	@Column(name="Address", length=255)
	private String address;

	@ManyToOne
	@JoinColumn(name="TypeId")
	private RealtyType type;

	public RealtyObject() {} // for Hibernate

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public RealtyType getType() {
		return type;
	}

	public void setType(RealtyType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String value = (type==null?"":(type.getName() + " - ")) + address;

		return value;
	}

}