package org.kesler.simplereg.logic.reception.filter;

import java.util.ArrayList;
import java.util.List;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Service;

public class ServiceReceptionsFilter implements ReceptionsFilter {

    private ReceptionsFiltersEnum filtersEnum = ReceptionsFiltersEnum.SERVICE;

	private final List<Service> filterServices;


	public ServiceReceptionsFilter(List<Service> filterServices) {
		this.filterServices = new ArrayList<Service>();
        this.filterServices.addAll(filterServices);
	}

	public List<Service> getServices() {
		return filterServices;
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
		
		for (Service service: filterServices) {
			if (service.equals(reception.getService())) {
				fit = true;
			}

		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По услугам: (";
		for (Service service: filterServices) {
			filterString += service.getName() + ";";
		}
		filterString += ")";

		return filterString;
	}

}