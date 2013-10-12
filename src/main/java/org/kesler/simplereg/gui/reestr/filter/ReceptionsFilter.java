package org.kesler.simplereg.gui.reestr.filter;

import org.kesler.simplereg.logic.reception.Reception;

public interface ReceptionsFilter {
	public boolean checkReception(Reception reception);
	@Override
	public String toString();
}