package org.kesler.simplereg.gui;

import java.util.List;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

import org.kesler.simplereg.logic.Reception;


class MainViewReceptionsTableModel extends AbstractTableModel {
	private List<Reception> receptions;

	public MainViewReceptionsTableModel() {
		this.receptions = new ArrayList<Reception>();
	}

	public void setReceptions(List<Reception> receptions) {
		this.receptions = receptions;
		fireTableDataChanged();
	}

	public int getRowCount() {
		return receptions.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public String getColumnName(int column) {
		String columnName = "Не опр";
		switch (column) {
				case 0: columnName = "№";
				break;
				case 1: columnName = "Создано";
				break;
				case 2: columnName = "ФИО заявителя";
				break;
				case 3: columnName = "Услуга";
				break;
			}
			return columnName;

	}

	public Object getValueAt(int row, int column) {
		Object value = null;
		Reception reception = receptions.get(row);
		if (reception != null) {
			switch (column) {
				case 0: value = reception.getId();
				break;
				case 1: value = reception.getOpenDate();
				break;
				case 2: value = reception.getApplicatorFIO();
				break;
				case 3: value = reception.getServiceName();
				break;
			}

		}

		return value;

	}
}