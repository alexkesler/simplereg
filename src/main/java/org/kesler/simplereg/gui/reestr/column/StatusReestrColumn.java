package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class StatusReestrColumn extends ReestrColumn {
	
	public StatusReestrColumn() {
		name = "Состояние";
		alias = "status";
		width = 20;
	}

	public String getValue(Reception reception) {
		return reception.getStatusName();
	}
}