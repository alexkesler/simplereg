package org.kesler.simplereg.logic.reception.filter;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.util.DateUtil;

import java.util.*;

import static org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersEnum.*;

public class ReceptionsFiltersModel {
    protected final Logger log;

    private List<ReceptionsFilter> filters;
    private Date fromOpenDate;
    private Date toOpenDate;

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
        filters.add(filter);
        return filters.size()-1;
    }

    public void removeFilter(int index) {
        log.info("Remove filter:" + filters.get(index));
        filters.remove(index);
    }

    public void resetFilters() {
        log.info("Reset filters");
        filters = new ArrayList<ReceptionsFilter>();
    }

    public Date getFromOpenDate () {
        return fromOpenDate;
    }
    public Date getToOpenDate () {
        return toOpenDate;
    }

    public void setFromOpenDate(Date fromDate) {
        fromOpenDate = DateUtil.toBeginOfDay(fromDate);
    }


    public void setToOpenDate(Date toDate) {
        toOpenDate = DateUtil.toEndOfDay(toDate);
    }

    private void initFilters() {

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH,-1);

        fromOpenDate = calendar.getTime();
        fromOpenDate = DateUtil.toBeginOfDay(fromOpenDate);

        toOpenDate = new Date();
        toOpenDate = DateUtil.toEndOfDay(toOpenDate);

    }

    public List<Reception> filterReceptions(List<Reception> allReceptions) {
        List<Reception> filteredReceptions = new ArrayList<Reception>();
        for (Reception reception: allReceptions) {
            boolean fit = true;
            // проверяем по датам
            Date openDate = reception.getOpenDate();
            if (openDate != null)
                if (openDate.before(fromOpenDate) || openDate.after(toOpenDate)) fit = false;

            // проверяем по фильтрам
            for (ReceptionsFilter filter: filters) {
                if (!filter.checkReception(reception)) {
                    fit = false;
                    break;
                }
            }

            if (fit) filteredReceptions.add(reception);
        }


        return filteredReceptions;
    }

}