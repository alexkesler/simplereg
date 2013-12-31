package org.kesler.simplereg.logic.reception.filter;

public class QuickReceptionsFilterFactory {
    public QuickReceptionsFilterFactory() {}

    public static ReceptionsFilter createQuickFilter(QuickReceptionsFiltersEnum filtersEnum, String filterString) {
        ReceptionsFilter filter = null;

        switch (filtersEnum) {
            case RECEPTION_CODE:
                filter = new CodeReceptionsFilter(filterString);
                break;
            case ROSREESTR_CODE:
                filter = new RosreestrCodeReceptionsFilter(filterString);
                break;
        }

        return filter;
    }

    public static Class<? extends ReceptionsFilter> getQuickFilterClass(QuickReceptionsFiltersEnum filtersEnum) {
        Class<? extends ReceptionsFilter> filterClass  = null;
        switch (filtersEnum) {
            case RECEPTION_CODE:
                filterClass = CodeReceptionsFilter.class;
                break;
            case ROSREESTR_CODE:
                filterClass = RosreestrCodeReceptionsFilter.class;
                break;
        }
        return filterClass;
    }
}