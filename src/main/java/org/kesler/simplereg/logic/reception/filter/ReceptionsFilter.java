package org.kesler.simplereg.logic.reception.filter;

import org.kesler.simplereg.logic.Reception;

public interface ReceptionsFilter {
	ReceptionsFiltersEnum getFiltersEnum();
	boolean checkReception(Reception reception);
	@Override
	String toString();
}