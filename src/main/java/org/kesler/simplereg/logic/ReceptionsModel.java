package org.kesler.simplereg.logic;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOFactory;


public ReceptionsModel extends AbstractTableModel {
	private List<Reception> receptions = null;
	private static receptionsModel = null;
	private 

	private ReceptionsModel(List<Reception> receptions) {
		this.receptions = receptions;
	}

	public static synchronized ReceptionsModel getInstance() {
		if (receptionsModel == null) {
			ReceptionDAO receptionDAO = DAOFactory.getInstance().getReceptionDAO();
			List<Reception> receptions = receptionDAO.getAllReceptions();
			receptionsModel = new ReceptionsModel(receptions);
		}

		return receptionsModel;
	}


}

