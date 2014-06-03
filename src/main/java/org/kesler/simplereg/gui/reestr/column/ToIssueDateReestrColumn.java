package org.kesler.simplereg.gui.reestr.column;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.kesler.simplereg.logic.Reception;

public class ToIssueDateReestrColumn extends ReestrColumn {

	public ToIssueDateReestrColumn() {
		name = "Выдать";
		alias = "toIssueDate";
		width = 10;
	}

	public String getValue(Reception reception) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date toIssueDate = reception.getToIssueDate();

		return toIssueDate==null?"Не опр":dateFormat.format(toIssueDate);
	}
}