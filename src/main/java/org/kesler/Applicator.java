package org.kesler;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Applicators")
public class Applicator {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private long id;

	@Column(name="FIO", length=255)
	private String fio;

	public Applicator() {
		// for Hibernate
	}

	public Applicator(long id, String fio) {
		this.id = id;
		this.fio = fio;
	}

	public Applicator(String fio) {
		this.fio = fio;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFIO() {
		return fio;
	}

	public void setFIO(String fio) {
		this.fio = fio;
	}
}