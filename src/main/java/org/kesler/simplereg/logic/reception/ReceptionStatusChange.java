package org.kesler.simplereg.logic.reception;

import org.hibernate.annotations.Proxy;
import org.kesler.simplereg.dao.AbstractEntity;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Reception;

import javax.persistence.*;
import java.util.Date;

@Entity
@Proxy(lazy = false)
@Table(name="ReceptionStatusChanges")
public class ReceptionStatusChange extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "ReceptionID", nullable = false)
    private Reception reception;

    @ManyToOne
    @JoinColumn (name = "ReceptionStatusID")
    private ReceptionStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ChangeTime")
    private Date changeTime;

    @ManyToOne
    @JoinColumn (name = "OperatorID")
    private Operator operator;

    public ReceptionStatusChange() {}

    public ReceptionStatusChange(Reception reception, ReceptionStatus status, Date changeTime, Operator operator) {
        this.reception = reception;
        this.status = status;
        this.changeTime = changeTime;
        this.operator = operator;
    }

    public Reception getReception() {return reception;}

    public ReceptionStatus getStatus() {return status;}

    public Date getChangeTime() {return changeTime;}

    public Operator getOperator() {return operator;}


}