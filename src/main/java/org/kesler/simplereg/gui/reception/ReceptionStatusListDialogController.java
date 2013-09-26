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

	}

	public void openEditStatusDialog() {

	}

}