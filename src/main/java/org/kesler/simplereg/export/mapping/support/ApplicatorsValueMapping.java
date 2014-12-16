package org.kesler.simplereg.export.mapping.support;

import org.kesler.simplereg.export.mapping.ValueMapping;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;

import java.util.ArrayList;
import java.util.List;


public class ApplicatorsValueMapping extends ValueMapping {
    public ApplicatorsValueMapping(Reception reception) {
        super(reception);
    }

    @Override
    public String getName() {
        return "@Applicators@";
    }

    @Override
    public String getValue() {
        String applicatorsString = "";
        List<FL> represes = new ArrayList<FL>();
        List<Applicator> applicators = reception.getApplicators();
        for (Applicator applicator: applicators) {
            applicatorsString = applicatorsString + applicator.getFullName() + "; ";
            FL repres = applicator.getRepres();
            if (repres==null && applicator instanceof ApplicatorFL) repres = ((ApplicatorFL) applicator).getFL();
            if (repres!=null) {
                if (!represes.contains(repres)) represes.add(repres);
            }
        }

        return applicatorsString;
    }
}
