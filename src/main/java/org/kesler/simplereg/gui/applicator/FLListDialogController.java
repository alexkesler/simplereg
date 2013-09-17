package org.kesler.simplereg.gui.applicator;

import java.util.List;
import javax.swing.JFrame;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.logic.applicator.FLModel;

class FLListDialogController {
	private FLModel model;
	private FLListDialog dialog;
	private static FLListDialogController instance = null;

	public static synchronized FLListDialogController getInstance() {
		if (instance == null) {
			instance = new FLListDialogController();
		}
		return instance;
	}

	private FLListDialogController() {
		model = FLModel.getInstance();
	}

	public List<FL> getFLList() {
		return model.getAllFLs();
	}

	public FL openDialog(JFrame frame) {
		dialog = new FLListDialog(frame, this);
		dialog.setVisible(true);
		return dialog.getFL();
	}

	public void openAddFLDialog() {
		FLDialog flDialog = new FLDialog(dialog);
		flDialog.setVisible(true);
		FL fl = flDialog.getFL();
		if (fl != null) {
			model.addFL(fl);
			dialog.addedFL(model.getAllFLs().indexOf(fl));
		}
	}

	public void openEditFLDialog(FL fl) {
		FLDialog flDialog = new FLDialog(dialog, fl);
		flDialog.setVisible(true);
		fl = flDialog.getFL();
		if (fl != null) {
			model.updateFL(fl);
			dialog.updatedFL(model.getAllFLs().indexOf(fl));
		}

	}

	public void deleteFL(FL fl) {

	}

}