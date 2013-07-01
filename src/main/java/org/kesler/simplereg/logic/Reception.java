package org.kesler.simplereg.logic;

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

import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Proxy(lazy=false)
@Table(name="Receptions")
public class Reception {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy="increment")
	private Long id;

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

	public Reception(Long id, Service service, Applicator applicator, Operator operator, Date openDate) {
		this.id = id;
		this.service = service;
		this.applicator = applicator;
		this.operator = operator;
		this.openDate = openDate;
	}

	public Reception(Service service, Applicator applicator, Operator operator, Date openDate) {
		this.service = service;
		this.applicator = applicator;
		this.operator = operator;
		this.openDate = openDate;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getServiceName() {
		String serviceName = "Услуга не определена";
		if (service!=null) {
			serviceName = service.getName();
		}
		return serviceName;

	}

	public Applicator getApplicator() {
		return applicator;
	}

	public String getApplicatorFIO() {
		String applicatorFIO = "Заявитель не определен";
		if (applicator!=null) {
			applicatorFIO = applicator.getFIO();
		}
		return applicatorFIO;
	}

	public void setApplicator(Applicator applicator) {
		this.applicator = applicator;
	}

	public Operator getOperator() {
		return operator;
	}

	public String getOperatorFIO() {
		String operatorFIO = "Оператор не определен";
		if (operator!=null) {
			operatorFIO = operator.getFIO();
		}
		return operatorFIO;
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