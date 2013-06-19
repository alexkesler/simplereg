package org.kesler;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Receptions")
public class Reception {
	private long id;
	private Service service;
	private Applicator applicator;
	private Operator operator;
	private Date openDate;

	public Reception() {
		// for Hibernate
	}

	public Reception(long id, Service service, Applicator applicator, Operator operator, Date openDate) {
		this.id = id;
		this.service = service;
		this.applicator = applicator;
		this.operator = operator;
		this.openDate = openDate;
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	//@?
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	//@?
	public Applicator getApplicator() {
		return applicator;
	}

	public void setApplicator(Applicator applicator) {
		this.applicator = applicator;
	}

	//@?
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OpenDate")
	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}


}