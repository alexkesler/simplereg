package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public class ApplicatorsReestrColumn extends ReestrColumn {

	public ApplicatorsReestrColumn() {
		name = "Заявители";
		alias = "applicators";
	}

	public String getValue(Reception reception) {
		return reception.getApplicatorsNames();
	}
}