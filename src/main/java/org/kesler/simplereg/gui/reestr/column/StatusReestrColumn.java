package org.kesler.simplereg.gui.reestr.column;

import org.kesler.simplereg.logic.Reception;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatusReestrColumn extends ReestrColumn {
	
	public StatusReestrColumn() {
		name = "Состояние";
		alias = "status";
		width = 20;
	}

	public String getValue(Reception reception) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String statusName = reception.getStatusName();
        Date changeDate = reception.getStatusChangeDate();
        String changeDateString = changeDate==null?"Не опр":dateFormat.format(changeDate);

		return statusName + " (" + changeDateString + ")";
	}
}