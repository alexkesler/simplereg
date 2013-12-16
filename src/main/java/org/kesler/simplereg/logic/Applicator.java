package org.kesler.simplereg.logic;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


import org.hibernate.envers.Audited;

import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.FL;

/**
* Представляет общий интерфейс для разных заявителей, которые могут быть связаны с приемом документов 
*/
@Entity

@Table (name = "Applicators")
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Applicator extends AbstractEntity {
	
	@ManyToOne
	@JoinColumn(name="ReceptionID", nullable=false)
	private Reception reception;

	public Reception getReception() {
		return reception;
	}

	public void setReception(Reception reception) {
		this.reception = reception;
	}

	public abstract String getName();

	public abstract String getFullName();

	public abstract FL getRepres();

	public abstract String toString();
} 	 