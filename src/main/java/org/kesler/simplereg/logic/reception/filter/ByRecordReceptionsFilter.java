package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public class ByRecordReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.BY_RECORD;

	private Boolean byRecord;

	public ByRecordReceptionsFilter(Boolean byRecord) {
		this.byRecord = byRecord;
	}

	public Boolean getByRecord() {
		return byRecord;
	}

	public void setByRecord(Boolean byRecord) {
		this.byRecord = byRecord;
	}

    @Override
    public ReceptionsFiltersEnum getFiltersEnum() {
        return filtersEnum;
    }

	@Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException("Reception argument can not be null");
		}

		Boolean receptionByRecord = reception.isByRecord();

		boolean fit = false;

		if (receptionByRecord != null && receptionByRecord == byRecord) {
			fit = true;			
		}		

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "";
		if (byRecord) {
			filterString = "Приемы по предварительной записи";
		} else {
			filterString = "Приемы не по предварительной записи";
		}

		return filterString;
	}

}