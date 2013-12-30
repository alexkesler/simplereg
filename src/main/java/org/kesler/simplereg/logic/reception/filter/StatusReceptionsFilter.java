package org.kesler.simplereg.logic.reception.filter;

import java.util.List;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class StatusReceptionsFilter implements ReceptionsFilter {

	private List<ReceptionStatus> filterReceptionStatuses;


	public StatusReceptionsFilter(List<ReceptionStatus> filterReceptionStatuses) {
		this.filterReceptionStatuses = filterReceptionStatuses;
	}

	public List<ReceptionStatus> getStatuses() {
		return filterReceptionStatuses;
	}

	@Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException();
		}

		boolean fit = false;
		
		for (ReceptionStatus receptionStatus: filterReceptionStatuses) {
			if (receptionStatus.equals(reception.getStatus())) {
				fit = true;
			}
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По состоянию дел: (";
		for (ReceptionStatus receptionStatus: filterReceptionStatuses) {
			filterString += receptionStatus.getName() + ";";
		}
		filterString += ")";

		return filterString;
	}

}