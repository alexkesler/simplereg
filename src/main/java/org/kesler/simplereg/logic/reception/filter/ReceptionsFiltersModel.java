package org.kesler.simplereg.logic.reception.filter;

import java.util.*;

public class ReceptionsFiltersModel {

    private List<ReceptionsFilter> filters;

    public ReceptionsFiltersModel() {
        filters = new ArrayList<ReceptionsFilter>();
        initFilters(); // Ограничиваем список закружаемых из БД приемов последним месяцем (по умолчанию)
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

    public Date getFromOpenDate () {
        Date fromOpenDate = null;

        for (ReceptionsFilter filter: filters) {
            if (filter instanceof OpenDateReceptionsFilter) {
                OpenDateReceptionsFilter openDateFilter = (OpenDateReceptionsFilter) filter;
                Date filterFromOpenDate =  openDateFilter.getFromDate();
                if (fromOpenDate == null || (fromOpenDate != null && filterFromOpenDate != null && fromOpenDate.getTime() < filterFromOpenDate.getTime()) )  {
                    fromOpenDate = filterFromOpenDate;
                }

            }
        }

        return fromOpenDate;
    }
    public Date getToOpenDate () {
        Date toOpenDate = null;

        for (ReceptionsFilter filter: filters) {
            if (filter instanceof OpenDateReceptionsFilter) {
                OpenDateReceptionsFilter openDateFilter = (OpenDateReceptionsFilter) filter;
                Date filterToOpenDate =  openDateFilter.getToDate();
                if (toOpenDate == null || (toOpenDate != null && filterToOpenDate != null && toOpenDate.getTime() > filterToOpenDate.getTime()) )
                    toOpenDate = filterToOpenDate;
            }
        }

        return toOpenDate;
    }

    private void initFilters() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH,-1);
        Date fromDate = calendar.getTime();
        Date toDate = new Date();
        ReceptionsFilter openDateFilter = new OpenDateReceptionsFilter(fromDate, toDate);
        filters.add(openDateFilter);
    }

}