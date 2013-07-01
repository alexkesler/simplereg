package org.kesler.simplereg.gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import org.kesler.simplereg.logic.Reception;


class MainViewReceptionsTableModel extends AbstractTableModel {
	private List<Reception> receptions;

	public MainViewReceptionsTableModel(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public void setReceptions(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public int getRowCount() {
		receptions.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int row, int column) {
		Object value = null;
		Reception reception = receptions.get(row);
		if (reception != null) {
			switch (column) {
				case 0: value = reception.getID();
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