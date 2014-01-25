package org.kesler.simplereg.logic.reception;

import java.util.List;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.util.OptionsUtil;

public class ReceptionStatusesModel {

	private static ReceptionStatusesModel instance = null;
	private List<ReceptionStatus> receptionStatuses;

	private ReceptionStatusesModel() {}

	public static synchronized ReceptionStatusesModel getInstance() {
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
		receptionStatuses = DAOFactory.getInstance().getReceptionStatusDAO().getAllItems();
		checkDefaultStatus();
	}


	public int addReceptionStatus(ReceptionStatus receptionStatus) {
		if (receptionStatuses == null) {
			readFromDB();
		}
		Long id = DAOFactory.getInstance().getReceptionStatusDAO().addItem(receptionStatus);
		if (id != null) {
			receptionStatuses.add(receptionStatus);
			return receptionStatuses.size()-1;
		} else {
			return -1;
		}

	}

	public void updateReceptionStatus(ReceptionStatus receptionStatus) {
		DAOFactory.getInstance().getReceptionStatusDAO().updateItem(receptionStatus);
	}

	public void removeReceptionStatus(ReceptionStatus receptionStatus) {
		DAOFactory.getInstance().getReceptionStatusDAO().removeItem(receptionStatus);
		receptionStatuses.remove(receptionStatus);
	}

	public ReceptionStatus getInitReceptionStatus() {
		readFromDB();
		Integer initRecStatCode = Integer.parseInt(OptionsUtil.getOption("logic.initRecStatusCode"));

		ReceptionStatus initReceptionStatus = null;

		for (ReceptionStatus status : receptionStatuses) {
				Integer code = status.getCode();
				if (code == initRecStatCode) initReceptionStatus = status;			
		}
		return initReceptionStatus;
	}

	private void checkDefaultStatus() {
		if (receptionStatuses == null) return;
		Integer initRecStatCode = Integer.parseInt(OptionsUtil.getOption("logic.initRecStatusCode"));
		if (receptionStatuses.size() == 0) {
			ReceptionStatus initReceptionStatus = new ReceptionStatus();
			initReceptionStatus.setCode(initRecStatCode);
			initReceptionStatus.setName("Новое");
			addReceptionStatus(initReceptionStatus);
		} else {
			boolean hasInitReceptionStatus = false;
			for (ReceptionStatus status: receptionStatuses) {
			}
			if (hasInitReceptionStatus) {
				ReceptionStatus initReceptionStatus = new ReceptionStatus();
				initReceptionStatus.setCode(initRecStatCode);
				initReceptionStatus.setName("Новое");
				addReceptionStatus(initReceptionStatus);				
			}
		}
	}
}