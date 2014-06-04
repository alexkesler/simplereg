package org.kesler.simplereg.logic;

import java.util.ArrayList;
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

import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusChange;

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

    @Column(name = "receptionCodeNum")
    private Integer receptionCodeNum;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="StatusChangeDate")
    private Date statusChangeDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reception")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    @Fetch(FetchMode.SUBSELECT)
    private List<ReceptionStatusChange> statusChanges;

	@Column(name="ByRecord")
	private Boolean byRecord;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ToIssueDate")
	private Date toIssueDate;

	@Column(name="RosreestrCode", length=25)
	private String rosreestrCode;

	@Column(name="ResultInMFC")
	private Boolean resultInMFC;

	@Column(name="FilialCode")
	private String filialCode;

    @ManyToOne
    @JoinColumn(name = "ParentReceptionID")
    private Reception parentReception;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentReception")
    @Cascade(CascadeType.SAVE_UPDATE)
    @Fetch(FetchMode.SUBSELECT)
    private List<Reception> subReceptions;


    public Reception() {
        applicators = new ArrayList<Applicator>();
        statusChanges = new ArrayList<ReceptionStatusChange>();
        subReceptions = new ArrayList<Reception>();
	}

	public Reception(Service service, List<Applicator> applicators, Operator operator, Date openDate) {
		this.service = service;
		this.applicators = applicators;
		this.operator = operator;
		this.openDate = openDate;
	}

	public void generateReceptionCode() {
		String serviceCode = "----";
        if (service!=null) serviceCode = service.getCode();

        String operatorCode = "--";
		if (operator!=null) operatorCode = operator.getCode();
//		int count = CounterUtil.getNextCount();
		String generatedCode = serviceCode + "/" + filialCode + "-" + operatorCode + "/" + receptionCodeNum;
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
		return operator==null?"":operator.getFIO();
	}

	/**
	* Возвращает строку содержащую список заявителей по данному делу
	*/
	public String getApplicatorsNames() {
		String names = "";
		for (int i = 0; i < applicators.size(); i++) {
			names += (i+1) + ". " + applicators.get(i).toString() + " "; 
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
        Date changeDate = new Date();
        this.statusChangeDate = changeDate;
        Operator currentOperator = CurrentOperator.getInstance().getOperator();

        // запоминаем изменение состояния
        ReceptionStatusChange statusChange = new ReceptionStatusChange(this, status, changeDate, currentOperator);
        statusChanges.add(statusChange);

        //Изменяем состояния подзапросов
        for (Reception subReception:subReceptions) {
            subReception.setStatus(status,changeDate,operator);
        }
	}

    public void setStatus(ReceptionStatus status, Date changeDate, Operator operator) {
        this.status = status;
        this.statusChangeDate = changeDate;

        ReceptionStatusChange statusChange = new ReceptionStatusChange(this, status, changeDate, operator);
        statusChanges.add(statusChange);

        //Изменяем состояния подзапросов
        for (Reception subReception:subReceptions) {
            subReception.setStatus(status,changeDate,operator);
        }

    }

    public Date getStatusChangeDate() {return statusChangeDate;}

    public List<ReceptionStatusChange> getStatusChanges() {return statusChanges;}

    public void removeLastStatusChange() {
        if (statusChanges.size() > 1) {
            ReceptionStatusChange change = statusChanges.get(statusChanges.size()-1);
            removeStatusChange(change);

            // Назначаем статус предпоследнего
            ReceptionStatusChange lastChange = statusChanges.get(statusChanges.size()-1);
            this.status = lastChange.getStatus();
            this.statusChangeDate = lastChange.getChangeTime();
        }
    }

    public void removeStatusChange(ReceptionStatusChange statusChange) {
        statusChanges.remove(statusChange);
        for (Reception subReception: subReceptions) {
            subReception.removeSameStatusChange(statusChange);
        }
    }

    public void removeSameStatusChange(ReceptionStatusChange sameStatusChange) {
        int index = -1;
        for (ReceptionStatusChange statusChange:statusChanges) {
            if(statusChange.getStatus()==sameStatusChange.getStatus() &&
                    statusChange.getChangeTime() == sameStatusChange.getChangeTime() &&
                    statusChange.getOperator() == sameStatusChange.getOperator())
                index = statusChanges.indexOf(statusChange);
        }
        if (index !=- 1) {
            if (index == statusChanges.size()-1) {
                removeLastStatusChange();
            } else {
                removeStatusChange(statusChanges.get(index));
            }
        }
    }

	public String getStatusName() {
		return status == null?"Не определено":status.getName();
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

    public Integer getReceptionCodeNum() {return receptionCodeNum;}

    public void setReceptionCodeNum(Integer receptionCodeNum) {this.receptionCodeNum = receptionCodeNum;}

	public Boolean isResultInMFC() {
		return resultInMFC;
	}

	public void setResultInMFC(Boolean resultInMFC) {
		this.resultInMFC = resultInMFC;
	}

	public String getFilialCode() {
		String notNullFilialCode = "";
		if (filialCode != null) notNullFilialCode = filialCode;
		return notNullFilialCode;
	}

	public void setFilialCode(String filialCode) {
		this.filialCode = filialCode;
	}

    public Reception getParentReception() {return parentReception;}
    public void setParentReception(Reception parentReception) {
        this.parentReception = parentReception;
        parentReception.addSubReception(this);
    }


    public List<Reception> getSubReceptions() {return subReceptions;}
    public void addSubReception(Reception subReception) {
        subReceptions.add(subReception);
    }

}