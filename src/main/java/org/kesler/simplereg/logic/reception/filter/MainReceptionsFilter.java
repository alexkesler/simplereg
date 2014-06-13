package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

/**
 * Фильтр для отбора основных дел
 */
public class MainReceptionsFilter implements ReceptionsFilter{
    ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.MAIN;

    private Boolean main;

    public MainReceptionsFilter(Boolean main) {
        this.main = main;
    }

    public Boolean getMain() {return main;}
    public void setMain(Boolean main) {this.main = main;}


    @Override
    public ReceptionsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }

    @Override
    public boolean checkReception(Reception reception) {
        if (reception == null) {
            throw new IllegalArgumentException("Reception argument can not be null");
        }

        if (main && reception.getParentReception()==null) return true;

        if (!main && reception.getParentReception()!=null) return true;

        return false;

    }
    @Override
    public String toString() {

        return main?"Только основные дела":"Только дополнительные дела";
    }

}
