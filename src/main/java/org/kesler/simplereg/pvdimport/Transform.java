package org.kesler.simplereg.pvdimport;

import org.kesler.simplereg.logic.*;
import org.kesler.simplereg.logic.ServicesModel;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.pvdimport.domain.Applicant;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.domain.Subject;
import org.kesler.simplereg.pvdimport.transform.TransformException;

public class Transform {
    public static Reception makeReceptionFromCause(Cause cause) throws TransformException{
        Reception reception = new Reception();

        reception.setService(getServiceByPackageType(cause.getPackage().getTypeId()));

        reception.setRosreestrCode(cause.getRegnum());

        for (Applicant applicant: cause.getApplicants()) {
            reception.getApplicators().add(getApplicatorForApplicant(applicant));
        }

        reception.setPvdPackageNum(cause.getPackageNum());

        return reception;
    }

    static Service getServiceByPackageType(String typeId) throws TransformException{
        for(Service service: ServicesModel.getInstance().getActiveServces()) {
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

    static FL getFLByFIO(String firstname, String surname, String parentName)  {
        FL fl = new FL();

        fl.setFirstName(firstname);
        fl.setSurName(surname);
        fl.setParentName(parentName);

        return fl;
    }

    static UL getULByShortName(String shortName) {
        UL ul = new UL();
        ul.setShortName(shortName);

        return ul;
    }
}
