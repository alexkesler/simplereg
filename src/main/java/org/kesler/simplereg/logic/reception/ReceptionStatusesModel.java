package org.kesler.simplereg.logic.reception;

import java.util.List;

import org.kesler.simplereg.dao.DAOFactory;

public class ReceptionStatusesModel {

	private static ReceptionStatusesModel instance = null;
	private List<ReceptionStatus> receptionStatuses;

	private ReceptionStatusesModel() {}

	public ReceptionStatusesModel getInstance() {
		if (instance == null) {
			instance = new ReceptionStatusesModel();
		}
		return instance;
	}

	public List<ReceptionStatus> getReceptionStatuses() {
		if (receptionStatuses == null) {
			readFromDB();
		}
		return receptionStatuses;
	}


	private void readFromDB() {
		receptionStatuses = DAOFactory.getInstance().getReceptionStatusDAO().getAllReceptionStatuses();
	}


	public int addReceptionStatus(ReceptionStatus receptionStatus) {
		if (receptionStatuses == null) {
			readFromDB();
		}
		Long id = DAOFactory.getInstance().getReceptionStatusDAO().addReceptionStatus(receptionStatus);
		if (id != null) {
			receptionStatuses.add(receptionStatus);
			return receptionStatuses.size()-1;
		} else {
			return -1;
		}

	}

	public void updateReceptionStatus(ReceptionStatus receptionStatus) {
		DAOFactory.getInstance().getReceptionStatusDAO().updateReceptionStatus(receptionStatus);
	}

	public void removeReceptionStatus(ReceptionStatus receptionStatus) {
		DAOFactory.getInstance().getReceptionStatusDAO().removeReceptionStatus(receptionStatus);
	}


}