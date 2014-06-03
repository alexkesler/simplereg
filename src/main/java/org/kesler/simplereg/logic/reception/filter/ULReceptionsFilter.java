package org.kesler.simplereg.logic.reception.filter;

import java.util.List;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;

public class ULReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.UL;

    private UL ul;


    public ULReceptionsFilter(UL ul) {
        this.ul = ul;
    }

    public UL getUl() {
        return ul;
    }

    @Override
    public ReceptionsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }


    @Override
    public boolean checkReception(Reception reception) {
        if (reception == null) {
            throw new IllegalArgumentException();
        }

        boolean fit = false;

        List<Applicator> applicators = reception.getApplicators();

        for(Applicator applicator: applicators) {
            if(applicator.getClass().equals(ApplicatorUL.class)) {
                UL currentUL = ((ApplicatorUL)applicator).getUL();
                 if (ul.equals(currentUL)) {
                    fit = true;
                    break;
                }
            }
        }

        return fit;
    }

    @Override
    public String toString() {
        String filterString  = "По юр. лицу: (" + ul.getShortName() + ")";

        return filterString;
    }

}