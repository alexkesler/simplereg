package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

public class ResultInMFCReestrColumn extends ReestrColumn {
	
	public ResultInMFCReestrColumn() {
		name = "Результат в МФЦ";
		alias = "resultInMFC";
		width = 10;
	}

	public String getValue(Reception reception) {
		String value = "";
		if (reception.isResultInMFC() == null) {
			value = "Не опр";
		} else if (reception.isResultInMFC()) {
			value = "Да";
		} else {
			value = "Нет";
		}

		return value;
	}
}