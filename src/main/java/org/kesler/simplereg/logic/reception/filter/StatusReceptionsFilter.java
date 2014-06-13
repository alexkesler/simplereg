package org.kesler.simplereg.logic.reception.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class StatusReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.STATUS;

	private List<ReceptionStatus> filterReceptionStatuses;
    private Date fromDate = null;
    private Date toDate = null;


	public StatusReceptionsFilter(List<ReceptionStatus> filterReceptionStatuses, Date fromDate, Date toDate) {
		this.filterReceptionStatuses = filterReceptionStatuses;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

	public List<ReceptionStatus> getStatuses() {
		return filterReceptionStatuses;
	}

    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    @Override
    public ReceptionsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }


    @Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException();
		}
		
		if (!filterReceptionStatuses.contains(reception.getStatus())) return false;

        Date statusChangeDate = reception.getStatusChangeDate();

        if (statusChangeDate == null) {// если время принятия не задано - не удовлетворяет
            return false;
        }

        if (fromDate != null && statusChangeDate.before(fromDate)) {
            return false;
        }

        if (toDate != null && statusChangeDate.after(toDate)) {
            return false;
        }


        return true;
	}

	@Override
	public String toString() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String statuses = "";
		for (ReceptionStatus receptionStatus: filterReceptionStatuses) {
			statuses += receptionStatus.getName() + ";";
		}

        String fromString = "";
        if (fromDate != null) {
            fromString = "с " + dateFormat.format(fromDate) + " ";
        }

        String toString = "";
        if (toDate != null) {
            toString = "по " + dateFormat.format(toDate);
        }


        return "По состоянию дел: (" + statuses + ")" + " (" + fromString + toString + ")";
	}

}