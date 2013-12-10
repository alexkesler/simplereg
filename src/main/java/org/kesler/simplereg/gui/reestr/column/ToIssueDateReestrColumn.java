package org.kesler.simplereg.gui.reestr.column;

import java.text.SimpleDateFormat;

import org.kesler.simplereg.logic.Reception;

public class ToIssueDateReestrColumn extends ReestrColumn {

	public ToIssueDateReestrColumn() {
		name = "Выдать";
		alias = "toIssueDate";
		width = 10;
	}

	public String getValue(Reception reception) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

		String value = "";

		if (reception.getToIssueDate() != null) {
			value = dateFormat.format(reception.getToIssueDate());
		} else {
			value = "Не опр";
		}

		return value;
	}
}