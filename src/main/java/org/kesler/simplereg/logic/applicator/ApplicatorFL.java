package org.kesler.simplereg.logic.applicator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.FL;

/**
* Класс представляет заявителя - физ. лица, привязанного к приему документов (включая наличие представителя при необходимости)
*/
@Entity
@Table(name = "ApplicatorsFL")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
@Proxy(lazy=false)
public class ApplicatorFL extends Applicator {

	
	@ManyToOne
	@JoinColumn(name = "FLID")
	private FL fl;

	@ManyToOne
	@JoinColumn(name = "RepresID")
	private FL repres;


	public ApplicatorFL() {}

	public FL getFL() {
		return fl;
	}

	public void setFL(FL fl) {
		this.fl = fl;
	}

	public FL getRepres() {
		return repres;
	}

	public void setRepres(FL repres) {
		this.repres = repres;
	}

	public String getFIO() {
		String fio = "Не определен";

		if (fl!=null) fio = fl.getFIO();

		return fio;
	}

	public String getRepresFIO() {
		String fio = "Не определен";

		if (repres!=null) fio = repres.getFIO();

		return fio;
	}

	@Override
	public String getName() {
		String name = "Не определен";

		if (fl!=null) name = fl.getShortFIO();

		return name;
	}

	@Override
	public String toString() {
		String applicatorFIO = "Не определен";
		if (fl!=null) applicatorFIO = fl.getFIO();

		String represFIO = "";
		if (repres!=null) represFIO = " (" + repres.getFIO() + ")";
		return applicatorFIO + represFIO;
	}
}