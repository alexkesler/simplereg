package org.kesler.simplereg.pvdimport;

import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.logic.*;

import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.applicator.FLModel;
import org.kesler.simplereg.logic.applicator.ULModel;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.service.ServicesModel;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Obj;
import org.kesler.simplereg.pvdimport.domain.Subject;
import org.kesler.simplereg.pvdimport.transform.TransformException;

import java.util.List;

public class Transform {
    public static Reception makeReceptionFromCause(Cause cause) throws TransformException{
        Reception reception = new Reception();

        reception.setOpenDate(cause.getBeginDate());

        reception.setOperator(CurrentOperator.getInstance().getOperator());

        reception.setService(getServiceByPackageType(cause.getPackage().getTypeId()));

        reception.setRosreestrCode(cause.getRegnum());

        for (Applicant applicant: cause.getApplicants()) {
            Applicator applicator = getApplicatorForApplicant(applicant);
            applicator.setReception(reception);
            reception.getApplicators().add(applicator);
        }


        Obj obj = cause.getObjects().size()==0?null:cause.getObjects().get(0);
        reception.setRealtyObject(getRealtyObjectForObj(obj));

        reception.setToIssueDate(cause.getEstimateDate());

        reception.setPvdPackageNum(cause.getPackageNum());

        return reception;
    }

    static Service getServiceByPackageType(String typeId) throws TransformException{
        List<Service> services = ServicesModel.getInstance().getActiveServices();
        for(Service service: services) {
            if (service.fitPkpvdTypeID(typeId)) return service;
        }
        throw new TransformException("Услуга не найдена");
    }

    static Applicator getApplicatorForApplicant(Applicant applicant) {
        Applicator applicator = null;

        Subject subject = applicant.getSubject();
        Subject agent = applicant.getAgent();

        String cls = subject.getClsType().substring(0,3);
        if (cls.equals("7.3")) { // Физ лицо
            ApplicatorFL applicatorFL = new ApplicatorFL();
            applicatorFL.setFL(getFLByFIO(subject.getFirstname(), subject.getSurname(), subject.getPatronymic()));
            if (agent!=null) applicatorFL.setRepres(getFLByFIO(agent.getFirstname(),agent.getSurname(),agent.getPatronymic()));
            applicator = applicatorFL;
        } else if (cls.equals("7.1") || cls.equals("7.2")) { // Юр лицо, муниципалитет
            ApplicatorUL applicatorUL = new ApplicatorUL();
            applicatorUL.setUL(getULByShortName(subject.getShortName()));
            applicatorUL.setRepres(getFLByFIO(agent.getFirstname(),agent.getSurname(),agent.getPatronymic()));
            applicator = applicatorUL;
        }


        return applicator;
    }

    static FL getFLByFIO(String firstName, String surName, String parentName)  {

        List<FL> fls = FLModel.getInstance().getFLsByFIO(firstName,surName,parentName);
        FL fl = null;
        if (fls.size()==0){
            fl = new FL();
            fl.setFirstName(firstName);
            fl.setSurName(surName);
            fl.setParentName(parentName);
            FLModel.getInstance().addFL(fl);
        } else {
            fl = fls.get(0);
        }

        return fl;
    }

    static UL getULByShortName(String shortName) {
        List<UL> uls = ULModel.getInstance().getULsByShortName(shortName);
        UL ul = null;
        if (uls.size()==0){
            ul = new UL();
            ul.setShortName(shortName);
            ULModel.getInstance().addUL(ul);
        } else {
            ul = uls.get(0);
        }


        return ul;
    }


    static RealtyObject getRealtyObjectForObj(Obj obj) {
        if (obj==null) return null;

        RealtyObject realtyObject = new RealtyObject();
        realtyObject.setAddress(obj.getFullAddress());
        RealtyObjectsModel.getInstance().addRealtyObject(realtyObject);

        return realtyObject;
    }
}
