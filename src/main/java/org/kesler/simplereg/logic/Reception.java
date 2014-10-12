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

import org.hibernate.annotations.*;

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

    @Column(name = "pvdPackageNum")
    private Integer pvdPackageNum;

	@ManyToOne
	@JoinColumn(name="ServiceID")
    @Fetch(FetchMode.JOIN)
	private Service service;

	@OneToMany (fetch = FetchType.EAGER, mappedBy="reception")
	@Cascade ({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
	@Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 20)
	private List<Applicator> applicators;

	@ManyToOne
	@JoinColumn(name="OperatorID")
    @Fetch(FetchMode.JOIN)
	private Operator operator;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="OpenDate")
	private Date openDate;

	@ManyToOne
	@JoinColumn(name="RealtyObjectID")
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 50)
	private RealtyObject realtyObject;

	@ManyToOne
	@JoinColumn(name="ReceptionStatusID")
	private ReceptionStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="StatusChangeDate")
    private Date statusChangeDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reception", orphanRemoval = true)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 50)
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

    @Column(name = "PvdtypeId")
    private String pvdtypeId;

    @Column(name = "PvdPurpose")
    private Integer pvdPurpose;

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
        // запоминаем дату изменения статуса и оператора
        Date changeDate = new Date();
        Operator currentOperator = CurrentOperator.getInstance().getOperator();

        setStatus(status,changeDate,currentOperator);
 	}

    private void setStatus(ReceptionStatus status, Date changeDate, Operator operator) {

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
            ReceptionStatusChange statusChange = statusChanges.get(statusChanges.size()-1);
            statusChanges.remove(statusChange);

            // Назначаем статус предпоследнего
            ReceptionStatusChange lastChange = statusChanges.get(statusChanges.size()-1);
            this.status = lastChange.getStatus();
            this.statusChangeDate = lastChange.getChangeTime();

            // удаляем похожее последнее изменение для подзапросов
            for (Reception subReception : subReceptions)
                subReception.removeLastSameStatusChange(statusChange);

        }
    }

    private void removeLastSameStatusChange(ReceptionStatusChange originStatusChange) {
        if (statusChanges.size() > 1) {
             ReceptionStatusChange statusChange = statusChanges.get(statusChanges.size()-1);
             if (statusChange.getChangeTime() == originStatusChange.getChangeTime() &&
                     statusChange.getStatus() == originStatusChange.getStatus() &&
                     statusChange.getOperator() == originStatusChange.getOperator()) {
                 statusChanges.remove(statusChange);
             }

            // Назначаем статус предпоследнего
            ReceptionStatusChange lastChange = statusChanges.get(statusChanges.size()-1);
            this.status = lastChange.getStatus();
            this.statusChangeDate = lastChange.getChangeTime();

            // удаляем похожее последнее изменение для подзапросов
            for (Reception subReception : subReceptions)
                subReception.removeLastSameStatusChange(originStatusChange);

        }


    }

    public Integer getPvdPackageNum() { return pvdPackageNum; }
    public void setPvdPackageNum(Integer pvdPackageNum) { this.pvdPackageNum = pvdPackageNum; }

    public String getStatusName() {
		return status == null?"Не определено":status.getName();
	}

	public Boolean isByRecord() {
		return byRecord;
	}
	public void setByRecord(Boolean byRecord) {
		this.byRecord = byRecord;
	}

	public Date getToIssueDate() { return toIssueDate; }
	public void setToIssueDate(Date toIssueDate) {	this.toIssueDate = toIssueDate; }

	public String getRosreestrCode() { return rosreestrCode; }
	public void setRosreestrCode(String rosreestrCode) { this.rosreestrCode = rosreestrCode; }

	public String getReceptionCode() { return receptionCode; }
	public void setReceptionCode(String receptionCode) { this.receptionCode = receptionCode; }

    public Integer getReceptionCodeNum() {return receptionCodeNum;}
    public void setReceptionCodeNum(Integer receptionCodeNum) {this.receptionCodeNum = receptionCodeNum;}

	public Boolean isResultInMFC() { return resultInMFC; }
	public void setResultInMFC(Boolean resultInMFC) { this.resultInMFC = resultInMFC; }

	public String getFilialCode() {
		String notNullFilialCode = "";
		if (filialCode != null) notNullFilialCode = filialCode;
		return notNullFilialCode;
	}

	public void setFilialCode(String filialCode) { this.filialCode = filialCode; }

    public String getPvdtypeId() { return pvdtypeId; }
    public void setPvdtypeId(String pvdtypeId) { this.pvdtypeId = pvdtypeId; }

    public Integer getPvdPurpose() { return pvdPurpose; }
    public void setPvdPurpose(Integer pvdPurpose) { this.pvdPurpose = pvdPurpose; }

    public Reception getParentReception() {return parentReception;}
    public void setParentReception(Reception parentReception) {
        if (parentReception!=null) {
            this.parentReception = parentReception;
            parentReception.addSubReception(this);
        } else {
            if (this.parentReception!=null) {
                this.parentReception.removeSubReception(this);
                this.parentReception = null;
            }
        }
    }

    public List<Reception> getSubReceptions() {return subReceptions;}
    public void addSubReception(Reception subReception) {
        subReceptions.add(subReception);
        subReception.parentReception = this;
    }
    public void removeSubReception(Reception subReception) {
        subReceptions.remove(subReception);
        subReception.parentReception=null;
    }

    public String getSubReceptionsRosreestrCodes() {
        String codes = "";

        for(Reception subReception:subReceptions)
            codes += subReception.getRosreestrCode() + "; ";

        return codes;
    }
}