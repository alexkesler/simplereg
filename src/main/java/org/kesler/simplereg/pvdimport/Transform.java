package org.kesler.simplereg.pvdimport;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.logic.*;

import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.applicator.FLService;
import org.kesler.simplereg.logic.applicator.ULService;
import org.kesler.simplereg.logic.realty.RealtyObjectsService;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.service.ServicesModel;
import org.kesler.simplereg.logic.support.ServiceException;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Obj;
import org.kesler.simplereg.pvdimport.domain.Subject;
import org.kesler.simplereg.pvdimport.transform.TransformException;

import java.util.Date;
import java.util.List;

public class Transform {
    private static Logger log = Logger.getLogger(Transform.class.getSimpleName());
    public static Reception makeReceptionFromCause(Cause cause) throws TransformException{
        log.info("Transform cause " + cause.getRegnum());
        Reception reception = new Reception();

//        reception.setOpenDate(cause.getBeginDate());
        reception.setOpenDate(new Date());

        reception.setOperator(CurrentOperator.getInstance().getOperator());

        reception.setService(getServiceByPackageTypePurpose(cause.getTypeId(),cause.getPurpose()));

        reception.setPvdtypeId(cause.getTypeId());
        reception.setPvdPurpose(cause.getPurpose());

        reception.setRosreestrCode(cause.getRegnum());

        reception.setParentReception(getReceptionByRosreestrCode(cause.getPrevCauseRegnum()));

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

    static Service getServiceByPackageTypePurpose(String typeId, Integer purpose) throws TransformException{
        List<Service> services = ServicesModel.getInstance().getActiveServices();
        for(Service service: services) {
            if (service.fitPvdtypePurpose(typeId, purpose)) return service;
        }
        return null;

//        throw new TransformException("Услуга не найдена");
    }

    static Reception getReceptionByRosreestrCode(String rosreestrCode) {
        if (rosreestrCode==null) return null;
        List<Reception> receptions = ReceptionsModel.getInstance().getReceptionsByRosreesrtCode(rosreestrCode);
        if (receptions.size()==0) return null;
        if (receptions.size()>0) return receptions.get(0);
        return null;
    }

    static Applicator getApplicatorForApplicant(Applicant applicant) throws TransformException{
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
            if (agent!=null) applicatorUL.setRepres(getFLByFIO(agent.getFirstname(),agent.getSurname(),agent.getPatronymic()));
            applicator = applicatorUL;
        }


        return applicator;
    }

    static FL getFLByFIO(String firstName, String surName, String parentName)  throws TransformException{

        List<FL> fls = null;
        try {
            fls = FLService.getInstance().getFLsByFIO(firstName,surName,parentName);
        } catch (ServiceException e) {
            throw new TransformException(e);
        }
        FL fl = null;
        if (fls.size()==0){
            fl = new FL();
            fl.setFirstName(firstName);
            fl.setSurName(surName);
            fl.setParentName(parentName);
            try {
                FLService.getInstance().addFL(fl);
            } catch (ServiceException e) {
                throw new TransformException(e);
            }
        } else {
            fl = fls.get(0);
        }

        return fl;
    }

    static UL getULByShortName(String shortName) throws TransformException{
        List<UL> uls = ULService.getInstance().getULsByShortName(shortName);
        UL ul = null;
        if (uls.size()==0){
            ul = new UL();
            ul.setShortName(shortName);
            ul.setFullName(shortName);
            try {
                ULService.getInstance().addUL(ul);
            } catch (ServiceException e) {
                throw new TransformException(e);
            }
        } else {
            ul = uls.get(0);
        }


        return ul;
    }


    static RealtyObject getRealtyObjectForObj(Obj obj) {
        if (obj==null) return null;

        RealtyObject realtyObject = new RealtyObject();
        realtyObject.setAddress(obj.getFullAddress());
        RealtyObjectsService.getInstance().addRealtyObject(realtyObject);

        return realtyObject;
    }
}
