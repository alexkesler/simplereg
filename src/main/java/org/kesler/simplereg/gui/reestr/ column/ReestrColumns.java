package org.kesler.simplereg.gui.reestr.column;

import java.util.List;
import java.util.ArrayList;


public class ReestrColumns {

	private List<ReestrColumn> allColumns;

	private List<ReestrColumn> activeColumns;

	private static ReestrColumns instance = null;

	public static synchronized ReestrColumns getInstance() {
		if (instance == null) {
			instance = new ReestrColumns();
		}

		return instance;
	}

	private ReestrColumns() {
		allColumns = new ArrayList<ReestrColumn>();
		allColumns.add(new OpenDateReestrColumn());
		allColumns.add(new ApplicatorsReestrColumn());
		allColumns.add(new ServiceReestrColumn());
		allColumns.add(new StatusReestrColumn());

		// временно - добавляем в активные поля все поля 
		activeColumns = new ArrayList<ReestrColumn>();
		for (ReestrColumn column: allColumns) {
			activeColumns.add(column);
		}
	}

	public List<ReestrColumn> getAllColumns() {
		return allColumns;
	}

	public List<ReestrColumn> getActiveColumns() {
		return activeColumns;
	}


}