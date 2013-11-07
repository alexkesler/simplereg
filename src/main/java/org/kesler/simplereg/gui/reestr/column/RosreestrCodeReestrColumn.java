package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.reception.Reception;

public class RosreestrCodeReestrColumn extends ReestrColumn {

	public RosreestrCodeReestrColumn() {
		name = "Код Росреестра";
		alias = "rosreestrCode";
		width = 15;
	}

	public String getValue(Reception reception) {
		
		String rosreestrCode = reception.getRosreestrCode();

		if (rosreestrCode == null) rosreestrCode = "Не опр.";

		return rosreestrCode;

	}
}