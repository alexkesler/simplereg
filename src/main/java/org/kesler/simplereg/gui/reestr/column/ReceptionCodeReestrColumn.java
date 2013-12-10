package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class ReceptionCodeReestrColumn extends ReestrColumn {

	public ReceptionCodeReestrColumn() {
		name = "Код дела";
		alias = "receptionCode";
		width = 10;
	}

	public String getValue(Reception reception) {
		
		String receptionCode = reception.getReceptionCode();

		if (receptionCode == null) receptionCode = "Не опр.";

		return receptionCode;

	}
}