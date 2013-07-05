package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.dao.AbstractEntity;

@Entity
@Proxy(lazy=false)
@Table(name="Applicators")
public class Applicator extends AbstractEntity{

	@Column(name="FIO", length=255)
	private String fio;

	public Applicator() {
		// for Hibernate
	}

	public Applicator(String fio) {
		this.fio = fio;
	}

	public String getFIO() {
		return fio;
	}

	public void setFIO(String fio) {
		this.fio = fio;
	}
}