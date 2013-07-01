package org.kesler.simplereg.logic;

import java.util.List;

import org.kesler.simplereg.dao.ReceptionDAO;
import org.kesler.simplereg.dao.DAOFactory;


public ReceptionsModel {
	private List<Reception> receptions = null;
	private static receptionsModel = null;

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

