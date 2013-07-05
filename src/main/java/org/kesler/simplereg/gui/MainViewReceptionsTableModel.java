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
	}

	public int getRowCount() {
		return receptions.size();
	}

	public int getColumnCount() {
		return 4;
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
				case 3: value = reception.getApplicatorFIO();
				break;
				case 4: value = reception.getServiceName();
				break;
			}

		}

		return value;

	}
}