package org.kesler.simplereg.logic.applicator;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Proxy;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.UL;

/**
* Класс представляет заявителя - юр. лица, привязанного к приему документов, включая информацию о представителе  
*/
@Entity
@Table (name = "ApplicatorsUL")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
@Proxy(lazy=false)
public class ApplicatorUL extends Applicator {
	
	@ManyToOne
	@JoinColumn(name="ULID")
	private UL ul;

	@ManyToOne
	@JoinColumn(name="RepresID")
	private FL repres;

	public ApplicatorUL() {}

	public UL getUL() {
		return ul;
	}

	public void setUL(UL ul) {
		this.ul = ul;
	} 

	@Override
	public FL getRepres() {
		return repres;
	}

	public void setRepres(FL repres) {
		this.repres = repres;
	}

	public String getRepresFIO() {
		String fio = "Не определен";

		if (repres!=null) fio = repres.getFIO();

		return fio;
	}	

	@Override
	public String getShortName() {
		String name = "Не определено";

		if (ul!=null) name = ul.getShortName();

		return name;
	}

	@Override
	public String getFullName() {
		String name = "Не определено";

		if (ul!=null) name = ul.getFullName();

		return name;
	}

	@Override
	public String toString() {
		String ulShortName = "";
		if (ul!=null) ulShortName = ul.getShortName();
		String represFIO = "";
		if (repres!=null) represFIO = " (" + repres.getFIO() + ")";

		return ulShortName + represFIO;

	}
}