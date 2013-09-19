package org.kesler.simplereg.gui.applicator;

import java.util.List;
import javax.swing.JFrame;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.logic.applicator.FLModel;

class FLListDialogController {
	private FLModel model;
	private FLListDialog dialog;
	private static FLListDialogController instance = null;

	private String filterString = "";

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
		if (filterString.isEmpty()) {
			return model.getAllFLs();
		} else {
			return model.getFilteredFLs();
		}
		
	}


	public FL openDialog(JFrame frame) {
		dialog = new FLListDialog(frame, this);
		dialog.setVisible(true);
		return dialog.getSelectedFL();
	} 

	public void filterFLList(String filter) {
		filterString = filter;
		model.filterFLList(filterString);
	}

	public void openAddFLDialog() {
		FLDialog flDialog = new FLDialog(dialog);
		flDialog.setVisible(true);
		if (flDialog.getResult() == FLDialog.OK) {
			FL fl = flDialog.getFL();
			int index = model.addFL(fl);
			if (index != -1) {
				dialog.addedFL(index);
			}
			
		}
	}

	public void openEditFLDialog(int index) {
		FL fl = model.getAllFLs().get(index);
		FLDialog flDialog = new FLDialog(dialog, fl);
		flDialog.setVisible(true);
		
		if (flDialog.getResult() == FLDialog.OK) {
			model.updateFL(fl);
			dialog.updatedFL(index);
		}

	}

	public void deleteFL(int index) {
		FL fl = model.getAllFLs().get(index);
		model.deleteFL(fl);
		dialog.removedFL(index);
	}

}