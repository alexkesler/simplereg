package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.Service;

public class ServiceReceptionsFilter implements ReceptionsFilter {

	private List<Service> filterServices;


	public ServiceReceptionsFilter(List<Service> filterServices) {
		this.filterServices = filterServices;
	}

	public List<Service> getServices() {
		return filterServices;
	}

	@Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException();
		}

		boolean fit = false;
		
		for (Service service: filterServices) {
			if (service.equals(reception.getService())) {
				fit = true;
			}
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По услугам: ";
		for (Service service: filterServices) {
			filterString += service.getName();
		}

		return filterString;
	}

}