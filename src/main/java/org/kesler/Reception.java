package org.kesler;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Receptions")
public class Reception {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private long id;

	@ManyToOne
	@JoinColumn(name="ServiceID")
	private Service service;

	@ManyToOne
	@JoinColumn(name="ApplicatorID")
	private Applicator applicator;
	
	@ManyToOne
	@JoinColumn(name="OperatorID")
	private Operator operator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OpenDate")
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Applicator getApplicator() {
		return applicator;
	}

	public void setApplicator(Applicator applicator) {
		this.applicator = applicator;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}


}