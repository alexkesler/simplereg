package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class OperatorReestrColumn extends ReestrColumn {
	
	public OperatorReestrColumn() {
		name = "Оператор";
		alias = "operator";
		width = 20;
	}

	public String getValue(Reception reception) {
		if (reception.getOperator() == null) {
			return "Не опеределен";
		}
		return reception.getOperator().getShortFIO();
	}
}