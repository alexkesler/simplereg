package org.kesler.simplereg.logic.reception.filter;

import org.apache.log4j.Logger;
import org.kesler.simplereg.util.DateUtil;

import java.util.*;

import static org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersEnum.*;

public class ReceptionsFiltersModel {
    protected final Logger log;

    private List<ReceptionsFilter> filters;
    private OpenDateReceptionsFilter openDateReceptionsFilter = null;

    public ReceptionsFiltersModel() {
        log = Logger.getLogger(this.getClass().getSimpleName());
        filters = new ArrayList<ReceptionsFilter>();
        initFilters(); // Ограничиваем список закружаемых из БД приемов последним месяцем (по умолчанию)
    }

    public List<ReceptionsFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }

    public int setQuickFilter(QuickReceptionsFiltersEnum quickFiltersEnum, String filterString) {
        log.info("Set quick filter: " + quickFiltersEnum + " ("+filterString+ ")");

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
        log.info("Reset quick filter: " + quickFiltersEnum);
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
        log.info("Adding filter: " + filter);
        if (filter.getFiltersEnum()== OPEN_DATE) {
            // назначаем новый фильтр по дате открытия
            openDateReceptionsFilter = (OpenDateReceptionsFilter) filter;
            // Подчищаем такие же фильтры из списка
            Iterator<ReceptionsFilter> filterIterator = filters.iterator();
            while (filterIterator.hasNext()) {
                if (filterIterator.next().getFiltersEnum()== OPEN_DATE) filterIterator.remove();
            }
        }
        filters.add(filter);
        return filters.size()-1;
    }

    public void removeFilter(int index) {
        log.info("Remove filter:" + filters.get(index));
        if (filters.get(index).getFiltersEnum()== OPEN_DATE) openDateReceptionsFilter = null;
        filters.remove(index);
    }

    public void resetFilters() {
        log.info("Reset filters");
        filters = new ArrayList<ReceptionsFilter>();
        openDateReceptionsFilter = null;
    }

    public Date getFromOpenDate () {
        return openDateReceptionsFilter==null?null:openDateReceptionsFilter.getFromDate();
    }
    public Date getToOpenDate () {
        return openDateReceptionsFilter==null?null:openDateReceptionsFilter.getToDate();
    }

    public void setOpenDates(Date fromDate, Date toDate) {
        fromDate = DateUtil.toBeginOfDay(fromDate);
        toDate = DateUtil.toEndOfDay(toDate);
        if(openDateReceptionsFilter==null) {
            openDateReceptionsFilter = new OpenDateReceptionsFilter(fromDate,toDate);
            filters.add(0,openDateReceptionsFilter);
        } else {
            openDateReceptionsFilter.setFromDate(fromDate);
            openDateReceptionsFilter.setToDate(toDate);
        }
    }

    private void initFilters() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH,-1);
        Date fromDate = calendar.getTime();
        fromDate = DateUtil.toBeginOfDay(fromDate);

        Date toDate = new Date();
        toDate = DateUtil.toEndOfDay(toDate);

        openDateReceptionsFilter = new OpenDateReceptionsFilter(fromDate, toDate);
        filters.add(openDateReceptionsFilter);
    }

}