package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public class OperatorReestrColumn extends ReestrColumn {
	
	public OperatorReestrColumn() {
		name = "Оператор";
		alias = "operator";
		width = 50;
	}

	public String getValue(Reception reception) {
		return reception.getOperator().getShortFIO();
	}
}