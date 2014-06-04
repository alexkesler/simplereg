package org.kesler.simplereg.logic.reception.filter;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.kesler.simplereg.logic.Reception;

public class OpenDateReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.OPEN_DATE;
	private Date fromDate = null;
	private Date toDate = null;

	public OpenDateReceptionsFilter(Date fromDate, Date toDate) {
		this.fromDate = fromDate;
		this.toDate = toDate;
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

		Date openDate = reception.getOpenDate();
		
		if (openDate == null) {// если время принятия не задано - не удовлетворяет
			return false;
		}

		if (fromDate != null && openDate.before(fromDate)) {
			return false;			
		}

		if (toDate != null && openDate.after(toDate)) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		String filterString = "По дате открытия дела: ";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		filterString += "(";

		if (fromDate != null) {
			filterString += "с " + dateFormat.format(fromDate) + " ";
		}

		if (toDate != null) {
			filterString += "по " + dateFormat.format(toDate);
		}

		filterString += ")";

		return filterString;
	}

}