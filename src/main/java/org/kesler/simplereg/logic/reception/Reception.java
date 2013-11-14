package org.kesler.simplereg.logic.reception;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.hibernate.envers.Audited;

import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.util.CounterUtil;

/**
* Класс предсталяет сущность приема заявителей
*/

@Entity
// @Audited
@Proxy(lazy=false)
@Table(name="Receptions")
public class Reception extends AbstractEntity{
	
	@Column(name="receptionCode", length=25)
	private String receptionCode;

	@ManyToOne
	@JoinColumn(name="ServiceID")
	private Service service;

	@OneToMany (fetch = FetchType.EAGER, mappedBy="reception")
	@Cascade ({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	@Fetch(FetchMode.SUBSELECT)
	private List<Applicator> applicators;

	@ManyToOne
	@JoinColumn(name="OperatorID")
	private Operator operator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OpenDate")
	private Date openDate;

	@ManyToOne
	@JoinColumn(name="RealtyObjectID")
	private RealtyObject realtyObject;

	@ManyToOne
	@JoinColumn(name="ReceptionStatusID")
	private ReceptionStatus status;

	@Column(name="ByRecord")
	private Boolean byRecord;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ToIssueDate")
	private Date toIssueDate;

	@Column(name="RosreestrCode", length=25)
	private String rosreestrCode;

	@Column(name="ResultInMFC")
	private Boolean resultInMFC;


	public Reception() {
		// for Hibernate
	}

	public Reception(Service service, List<Applicator> applicators, Operator operator, Date openDate) {
		this.service = service;
		this.applicators = applicators;
		this.operator = operator;
		this.openDate = openDate;
	}

	public void generateReceptionCode() {
		String operatorCode = "-----";
		if (operator!=null) operatorCode = operator.getCode();
		int count = CounterUtil.getNextCount();
		String generatedCode = "07-11/1/" + operatorCode + "/" + count;
		receptionCode = generatedCode;
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

	public List<Applicator> getApplicators() {
		return applicators;
	}

	/**
	* Устанавливает список заявителей, для каждого заявителя назначает свойство reception на this
	*/ 
	public void setApplicators(List<Applicator> applicators) {
		this.applicators = applicators;
		for (Applicator a: applicators) {
			a.setReception(this);
		}
	}

	public Operator getOperator() {
		return operator;
	}

	/**
	* Возвращает ФИО оператора, если оператор не присоединен, возвращает пустую строку
	*/
	public String getOperatorFIO() {
		String operatorFIO = "";
		if (operator!=null) {
			operatorFIO = operator.getFIO();
		}
		return operatorFIO;
	}

	/**
	* Возвращает строку содержащую список заявителей по данному делу
	*/
	public String getApplicatorsNames() {
		String names = "";
		for (int i = 0; i < applicators.size(); i++) {
			names += (i+1) + ". " + applicators.get(i).getName() + " "; 
		}

		return names;
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

	public RealtyObject getRealtyObject () {
		return realtyObject;
	}

	public void setRealtyObject(RealtyObject realtyObject) {
		this.realtyObject = realtyObject;
	}

	public ReceptionStatus getStatus() {
		return status;
	}

	public void setStatus(ReceptionStatus status) {
		this.status = status;
	}

	public String getStatusName() {
		String statusName = "Не определено";
		if (status != null) {
			statusName = status.getName();
		}
		return statusName;
	}

	public Boolean isByRecord() {
		return byRecord;
	}

	public void setByRecord(Boolean byRecord) {
		this.byRecord = byRecord;
	}

	public Date getToIssueDate() {
		return toIssueDate;
	}

	public void setToIssueDate(Date toIssueDate) {
		this.toIssueDate = toIssueDate;
	}

	public String getRosreestrCode() {
		return rosreestrCode;
	}

	public void setRosreestrCode(String rosreestrCode) {
		this.rosreestrCode = rosreestrCode;
	}

	public String getReceptionCode() {
		return receptionCode;
	}

	public void setReceptionCode(String receptionCode) {
		this.receptionCode = receptionCode;
	}

	public Boolean isResultInMFC() {
		return resultInMFC;
	}

	public void setResultInMFC(Boolean resultInMFC) {
		this.resultInMFC = resultInMFC;
	}


}