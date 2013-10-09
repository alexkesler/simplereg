package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public class ServiceReestrColumn extends ReestrColumn {

	public ServiceReestrColumn() {
		name = "Услуга";
		alias = "service";
	}

	public String getValue(Reception reception) {
		return reception.getServiceName();
	}

}