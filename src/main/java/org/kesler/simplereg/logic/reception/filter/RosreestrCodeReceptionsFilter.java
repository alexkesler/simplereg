package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public class RosreestrCodeReceptionsFilter implements ReceptionsFilter {

    private String filterRosreestrCode;


    public RosreestrCodeReceptionsFilter(String filterRosreestrCode) {
        this.filterRosreestrCode = filterRosreestrCode;
    }

    public String getRosreestrCode() {
        return filterRosreestrCode;
    }

    public void setRosreestrCode(String filterRosreestrCode) {
        this.filterRosreestrCode = filterRosreestrCode;
    }

    @Override
    public boolean checkReception(Reception reception) {
        if (reception == null) {
            throw new IllegalArgumentException();
        }

        boolean fit = false;

        if (reception.getRosreestrCode().toLowerCase().contains(filterRosreestrCode.toLowerCase())) {
            fit = true;
        }

        return fit;
    }

    @Override
    public String toString() {

        String filterString  = "По коду Росреестра: (" + filterRosreestrCode + ")";

        return filterString;

    }

}