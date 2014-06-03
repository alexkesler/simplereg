package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public class FilialReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.FILIAL;
	private String filterFilialCode;


	public FilialReceptionsFilter(String filterFilialCode) {
		this.filterFilialCode = filterFilialCode;
	}

	public String getFilialCode() {
		return filterFilialCode;
	}

	public void setFilialCode(String filterFilialCode) {
		this.filterFilialCode = filterFilialCode;
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
		
		if (reception.getFilialCode().equals(filterFilialCode)) {
			fit = true;
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По коду филиала: (" + filterFilialCode + ")";


		return filterString;
	}

}