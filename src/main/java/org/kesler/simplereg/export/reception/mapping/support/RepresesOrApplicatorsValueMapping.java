package org.kesler.simplereg.export.reception.mapping.support;

import org.kesler.simplereg.export.reception.mapping.ValueMapping;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;

import java.util.ArrayList;
import java.util.List;

public class RepresesOrApplicatorsValueMapping extends ValueMapping {
    public RepresesOrApplicatorsValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {

        return "@RepresesOrApplicators@";
    }

    @Override
    public String getValue() {
        List<FL> represes = new ArrayList<FL>();
        List<Applicator> applicators = reception.getApplicators();
        for (Applicator applicator: applicators) {
            FL repres = applicator.getRepres();
            if (repres==null && applicator instanceof ApplicatorFL) repres = ((ApplicatorFL) applicator).getFL();
            if (repres!=null) {
                if (!represes.contains(repres)) represes.add(repres);
            }
        }

        String represesString = "";
        for (FL repres: represes) {
            represesString = represesString + repres.getShortFIO() + " _____________; ";
        }

        return represesString;
    }
}
