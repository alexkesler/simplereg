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
		// Возвращаем фильтрованный список
		return model.getFilteredFLs();
		
	}

	/**
	* Открывает диалог заявителя - физического лица
	*/
	public FL openDialog(JFrame frame) {
		filterFLList("");
		dialog = new FLListDialog(frame, this);
		dialog.setVisible(true);
		return dialog.getSelectedFL();
	} 

	/**
	* Создает в модели фильтрованный список
	* @param filter определяет фильтр для записей. Если строка пустая - модель будет возвращать полный список.
	*/
	public void filterFLList(String filter) {
		model.filterFLList(filter.trim());
	}

	/**
	* Открывает диалог добавления нового физического лица
	*/
	public void openAddFLDialog() {
		FLDialog flDialog = new FLDialog(dialog);
		flDialog.setVisible(true);
		if (flDialog.getResult() == FLDialog.OK) {
			// очищаем фильтр
			filterFLList("");
			// запоминаем новое физ лицо		
			FL fl = flDialog.getFL();
			int index = model.addFL(fl);
			if (index != -1) {
				dialog.addedFL(index);
			}
			
		}
	}

	/**
	* Открывает диалог добавления физического лица с введенной фамилией
	* @param initSurName строка, на основнии которой создается фамилия 
	*/
	public void openAddFLDialog(String initSurName) {
		initSurName = initSurName.toLowerCase();
		String firstLetter = initSurName.substring(0,1);
		firstLetter = firstLetter.toUpperCase();
		initSurName = firstLetter + initSurName.substring(1, initSurName.length());
		FLDialog flDialog = new FLDialog(dialog, initSurName);
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