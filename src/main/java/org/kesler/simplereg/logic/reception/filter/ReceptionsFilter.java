package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public interface ReceptionsFilter {
    public ReceptionsFiltersEnum getFiltersEnum();
	public boolean checkReception(Reception reception);
	@Override
	public String toString();
}