package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public class StatusReestrColumn extends ReestrColumn {
	
	public StatusReestrColumn() {
		name = "Статус";
		alias = "status";
	}

	public String getValue(Reception reception) {
		return reception.getStatusName();
	}
}