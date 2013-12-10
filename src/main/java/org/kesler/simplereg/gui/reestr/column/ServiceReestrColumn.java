package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class ServiceReestrColumn extends ReestrColumn {

	public ServiceReestrColumn() {
		name = "Услуга";
		alias = "service";
		width = 70;
	}

	public String getValue(Reception reception) {
		return reception.getServiceName();
	}

}