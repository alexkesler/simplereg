package org.kesler.simplereg.gui.reestr.filter;

import java.util.List;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Operator;

public class OperatorReceptionsFilter implements ReceptionsFilter {

	private List<Operator> filterOperators;


	public OperatorReceptionsFilter(List<Operator> filterOperators) {
		this.filterOperators = filterOperators;
	}

	public List<Operator> getOperators() {
		return filterOperators;
	}

	@Override
	public boolean checkReception(Reception reception) {
		if (reception == null) {
			throw new IllegalArgumentException();
		}

		boolean fit = false;
		
		for (Operator operator: filterOperators) {
			if (operator.equals(reception.getOperator())) {
				fit = true;
			}
		}

		return fit;
	}

	@Override
	public String toString() {
		String filterString  = "По операторам: (";
		for (Operator operator: filterOperators) {
			filterString += operator.getShortFIO() + ";";
		}
		filterString += ")";

		return filterString;
	}

}