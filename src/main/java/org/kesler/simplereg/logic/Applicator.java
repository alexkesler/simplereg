package org.kesler.simplereg.logic;

import javax.persistence.*;


import org.kesler.simplereg.dao.AbstractEntity;

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

	@Column(name = "Issued")
	private Boolean issued;

	public Reception getReception() {
		return reception;
	}
	public void setReception(Reception reception) {
		this.reception = reception;
	}

	public Boolean isIssued() { return issued; }
	public void setIssued(Boolean issued) { this.issued = issued; }

	public abstract Applicator copyThis();

	public abstract String getShortName();

	public abstract String getFullName();

	public abstract FL getRepres();

	public abstract String toString();
} 	 