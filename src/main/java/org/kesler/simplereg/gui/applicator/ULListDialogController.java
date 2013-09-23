package org.kesler.simplereg.gui.applicator;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.kesler.simplereg.logic.applicator.UL;
import org.kesler.simplereg.logic.applicator.ULModel;

public class ULListDialogController {

	private static ULListDialogController instance = null;

	private ULListDialog dialog;
	private ULModel model;

	public static synchronized ULListDialogController getInstance() {
		if (instance == null) {
			instance = new ULListDialogController();
		}
		return instance;
	}

	private ULListDialogController() {
		model = ULModel.getInstance();
	}

	public UL openDialog(JFrame parentFrame) {
		dialog = new ULListDialog(parentFrame, this);
		dialog.setVisible(true);
		UL ul = null;
		if (dialog.getResult() == ULListDialog.OK) {
			ul = dialog.getSelectedUL();
		}

		return ul;

	}

	public List<UL> getULList() {
		// Возвращаем фильтрованный список
		return model.getFilteredULs();
		
	}


	public void filterULList(String filterString) {
		model.filterULList(filterString);
	}

	public void openAddULDialog() {
		ULDialog ulDialog = new ULDialog(dialog);
		ulDialog.setVisible(true);
		if (ulDialog.getResult() == ULDialog.OK) {
			filterULList("");
			UL ul = ulDialog.getUL();
			int index = model.addUL(ul);
			if (index != -1) {
				dialog.addedUL(index);
			}
			
		}

	}

	public void openEditULDialog(int index ) {
		UL ul = model.getAllULs().get(index);
		ULDialog ulDialog = new ULDialog(dialog, ul);
		ulDialog.setVisible(true);
		
		if (ulDialog.getResult() == ULDialog.OK) {
			model.updateUL(ul);
			dialog.updatedUL(index);
		}

	}

	public void deleteUL(int index) {
		UL ul = model.getAllULs().get(index);
		model.deleteUL(ul);
		dialog.removedUL(index);

	}


}