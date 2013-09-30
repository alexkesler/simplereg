package org.kesler.simplereg.gui.reception;

import java.util.List;

import javax.swing.JFrame;

import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionStatusesModel;

public class ReceptionStatusListDialogController {

	private ReceptionStatusListDialog dialog;
	private ReceptionStatusesModel model;


	private static ReceptionStatusListDialogController instance;

	private ReceptionStatusListDialogController() {
		model = ReceptionStatusesModel.getInstance();
	}

	public static synchronized ReceptionStatusListDialogController getInstance() {
		if (instance == null) {
			instance = new ReceptionStatusListDialogController();
		}
		return instance;
	}

	public void openDialog(JFrame frame) {
		dialog = new ReceptionStatusListDialog(frame, this);
		dialog.setVisible(true);
	}

	public List<ReceptionStatus> getReceptionStatuses() {
		return model.getReceptionStatuses();
	}

	public void openAddStatusDialog() {
		ReceptionStatusDialog statusDialog = new ReceptionStatusDialog(dialog);
		statusDialog.setVisible(true);
		if (statusDialog.getResult() == ReceptionStatusDialog.OK) {
			int index = model.addReceptionStatus(statusDialog.getReceptionStatus());
			if (index != -1) {
				dialog.addedStatus(index);
			}
		}
	}

	public void openEditStatusDialog(int index) {
		ReceptionStatus receptionStatus = getReceptionStatuses().get(index);
		ReceptionStatusDialog statusDialog = new ReceptionStatusDialog(dialog, receptionStatus);
		statusDialog.setVisible(true);
		if (statusDialog.getResult() == ReceptionStatusDialog.OK) {
			model.updateReceptionStatus(receptionStatus);
			dialog.updatedStatus(index);
		}
	}

	public void removeReceptionStatus(int index) {
		ReceptionStatus receptionStatus = getReceptionStatuses().get(index);
		model.removeReceptionStatus(receptionStatus);
		dialog.removedStatus(index);
	}

}