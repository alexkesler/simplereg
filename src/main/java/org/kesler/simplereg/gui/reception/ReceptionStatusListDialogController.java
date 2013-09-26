package org.kesler.simplereg.gui.reception;

import java.util.List;

import javax.swing.JFrame;

import org.kesler.simplereg.logic.reception.ReceptionStatus;

public class ReceptionStatusListDialogController {

	private ReceptionStatusListDialog dialog;


	private static ReceptionStatusListDialogController instance;

	private ReceptionStatusListDialogController() {
	}

	public ReceptionStatusListDialogController getInstance() {
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

	

}