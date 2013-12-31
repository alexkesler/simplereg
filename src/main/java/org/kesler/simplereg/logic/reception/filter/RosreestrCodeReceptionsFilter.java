package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public class RosreestrCodeReceptionsFilter implements ReceptionsFilter {

    private String rosreestrCodeFilter;


    public RosreestrCodeReceptionsFilter(String rosreestrCodeFilter) {
        this.rosreestrCodeFilter = rosreestrCodeFilter;
    }

    public String getRosreestrCode() {
        return rosreestrCodeFilter;
    }

    public void setRosreestrCode(String filterRosreestrCode) {
        this.rosreestrCodeFilter = filterRosreestrCode;
    }

    @Override
    public boolean checkReception(Reception reception) {
        if (reception == null) {
            throw new IllegalArgumentException();
        }

        boolean fit = false;

        String rosreestrCode = reception.getRosreestrCode();
        if (rosreestrCode != null && rosreestrCode.toLowerCase().contains(rosreestrCodeFilter.toLowerCase())) {
            fit = true;
        }

        return fit;
    }

    @Override
    public String toString() {

        String filterString  = "По коду Росреестра: (" + rosreestrCodeFilter + ")";

        return filterString;

    }

}