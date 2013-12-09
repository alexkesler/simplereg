package org.kesler.simplereg.gui.applicator;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.kesler.simplereg.logic.applicator.UL;
import org.kesler.simplereg.logic.applicator.ULModel;

import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;

public class ULListDialogController implements GenericListDialogController {

	private static ULListDialogController instance = null;

	private GenericListDialog dialog;
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

	public void openDialog(JDialog parentDialog) {
		dialog = new GenericListDialog<UL>(parentDialog, "Юр лица", this, GenericListDialog.VIEW_FILTER_MODE);

		model.readFromDB();
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);

		dialog.setVisible(true);
		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	public void openDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<UL>(parentFrame, "Юр лица", this, GenericListDialog.VIEW_FILTER_MODE);

		model.readFromDB();
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);

		dialog.setVisible(true);

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	public UL openSelectDialog(JDialog parentDialog) {
		dialog = new GenericListDialog<UL>(parentDialog, "Юр лица", this, GenericListDialog.SELECT_FILTER_MODE);

		model.readFromDB();
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);

		dialog.setVisible(true);

		UL ul = null;
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			ul = model.getFilteredULs().get(selectedIndex);
		}

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
		return ul;
	}

	public UL openSelectDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<UL>(parentFrame, "Юр лица", this, GenericListDialog.SELECT_FILTER_MODE);

		model.readFromDB();
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);

		dialog.setVisible(true);

		UL ul = null;
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			ul = model.getFilteredULs().get(selectedIndex);
		}

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
		return ul;

	}

	// public List<UL> getULList() {
	// 	// Возвращаем фильтрованный список
	// 	return model.getFilteredULs();
		
	// }

	@Override
	public void readItems() {
		model.readFromDB();
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);
	}

	@Override
	public void filterItems(String filterString) {
		model.setFilterString(filterString);
		model.filterULs();
		List<UL> uls = model.getFilteredULs();
		dialog.setItems(uls);
	}

	@Override
	public boolean openAddItemDialog() {
		boolean result = false;
		ULDialog ulDialog = new ULDialog(dialog);
		ulDialog.setVisible(true);
		if (ulDialog.getResult() == ULDialog.OK) {
			filterItems("");
			UL ul = ulDialog.getUL();
			int index = model.addUL(ul);
			if (index != -1) {
				dialog.addedItem(index);
				result = true;
			}
			
		}
		// Освобождаем ресурсы
		ulDialog.dispose();
		ulDialog = null;

		return result;
	}

	@Override
	public boolean openEditItemDialog(int index ) {
		boolean result = false;
		UL ul = model.getAllULs().get(index);
		ULDialog ulDialog = new ULDialog(dialog, ul);
		ulDialog.setVisible(true);
		
		if (ulDialog.getResult() == ULDialog.OK) {
			model.updateUL(ul);
			dialog.updatedItem(index);
			result = true;
		}
		// Освобождаем ресурсы
		ulDialog.dispose();
		ulDialog = null;

		return result;
	}

	@Override
	public boolean removeItem(int index) {
		UL ul = model.getAllULs().get(index);
		model.deleteUL(ul);
		dialog.removedItem(index);
		return true;
	}


}