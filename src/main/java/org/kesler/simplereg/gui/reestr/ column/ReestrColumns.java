package org.kesler.simplereg.gui.reestr.column;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class ReestrColumns {

	private List<ReestrColumn> allColumns;

	private List<ReestrColumn> activeColumns;
	private List<ReestrColumn> inactiveColumns;

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
		allColumns = Collections.unmodifiableList(allColumns); /// делаем полный список колонок неизменным во избежание

		// временно - добавляем в активные поля все поля 
		activeColumns = new ArrayList<ReestrColumn>();
		for (ReestrColumn column: allColumns) {
			activeColumns.add(column);
		}

		// список неактивных полей
		inactiveColumns = new ArrayList<ReestrColumn>();
	}

	public List<ReestrColumn> getAllColumns() {
		return allColumns;
	}

	public List<ReestrColumn> getActiveColumns() {
		return activeColumns;
	}

	public List<ReestrColumn> getInactiveColumns() {
		return inactiveColumns;
	}


	public boolean activateColumn(ReestrColumn column) {
		if (inactiveColumns.contains(column)) {
			inactiveColumns.remove(column);
			activeColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

	public boolean deactivateColumn(ReestrColumn column) {
		if (activeColumns.contains(column)) {
			activeColumns.remove(column);
			inactiveColumns.add(column);
			return true;
		} else {
			return false;
		}
	}

}