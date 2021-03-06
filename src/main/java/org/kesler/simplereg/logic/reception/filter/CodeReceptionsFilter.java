package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public class CodeReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.CODE;
	private String filterCode;


	public CodeReceptionsFilter(String filterCode) {
		this.filterCode = filterCode;
	}

	public String getCode() {
		return filterCode;
	}

	public void setCode(String filterCode) {
		this.filterCode = filterCode;
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

		boolean fit = false;
		String receptionCode = reception.getReceptionCode();
		if (receptionCode != null && receptionCode.toLowerCase().contains(filterCode.toLowerCase())) {
			fit = true;
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По коду дела: (" + filterCode + ")";


		return filterString;
	}

}