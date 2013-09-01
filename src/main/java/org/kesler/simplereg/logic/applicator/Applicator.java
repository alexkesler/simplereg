package org.kesler.simplereg.logic.applicator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.logic.Reception;

@Entity
@Table (name = "Applicators")
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Applicator extends AbstractEntity {
	
	@ManyToOne
	@JoinColumn(name="ReceptionID")
	private Reception reception;

	public Reception getReception() {
		return reception;
	}

	public void setReception(Reception reception) {
		this.reception = reception;
	}

	public abstract String getName();
} 	 