package org.kesler.simplereg.logic.reception.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReceptionsFiltersModel {

    private List<ReceptionsFilter> filters;

    public ReceptionsFiltersModel() {
        filters = new ArrayList<ReceptionsFilter>();
    }

    public List<ReceptionsFilter> getFilters() {
        return filters;
    }

    public int setQuickFilter(QuickReceptionsFiltersEnum quickFiltersEnum, String filterString) {

        ReceptionsFilter quickFilter = QuickReceptionsFilterFactory.createQuickFilter(quickFiltersEnum, filterString);

        Class<? extends ReceptionsFilter> filterClass = quickFilter.getClass();
        int index = -1;

        for (int i = 0; i < filters.size(); i++) {
            ReceptionsFilter f = filters.get(i);
            if (f.getClass().equals(filterClass)) {
                index = i;
            }
        }

        if (index != -1) {
            filters.remove(index);
            filters.add(index, quickFilter);
        } else {
            filters.add(0, quickFilter);
            index = filters.size()-1;
        }

        return index;
    }

    public int resetQuickFilter(QuickReceptionsFiltersEnum quickFiltersEnum) {
        Class<? extends ReceptionsFilter> filterClass = QuickReceptionsFilterFactory.getQuickFilterClass(quickFiltersEnum);
        Iterator<ReceptionsFilter> iterator = filters.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            ReceptionsFilter filter = iterator.next();
            if (filterClass.equals(filter.getClass())) {
                index = filters.indexOf(filter);
                iterator.remove();
            }
        }
        return index;
    }

    public int addFilter(ReceptionsFilter filter) {
        filters.add(filter);
        return filters.size()-1;
    }

    public void removeFilter(int index) {
        filters.remove(index);
    }

    public void resetFilters() {
        filters = new ArrayList<ReceptionsFilter>();
    }

}