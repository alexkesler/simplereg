package org.kesler.simplereg.gui.applicator;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JDialog;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.logic.applicator.FLModel;

import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;


public class FLListDialogController implements GenericListDialogController{
	
	private FLModel model;
	private GenericListDialog dialog;
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
	public FL openDialog(JDialog parentDialog) {
		filterFLList("");
		dialog = new GenericListDialog<FL>(parentDialog, this);
		dialog.setVisible(true);
		// получаем выбронное физ лицо
		FL selectedFL = dialog.getSelectedFL();
		
		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return selectedFL;
	} 

	/**
	* Создает в модели фильтрованный список
	* @param filter определяет фильтр для записей. Если строка пустая - модель будет возвращать полный список.
	*/
	@Override
	public void filterItems(String filter) {
		model.filterFLList(filter.trim());
	}

	/**
	* Открывает диалог добавления нового физического лица
	*/
	@Override
	public void openAddItemDialog() {
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
		flDialog.dispose();
		flDialog = null;
	}

	/**
	* Открывает диалог добавления физического лица с введенной фамилией
	* @param initSurName строка, на основнии которой создается фамилия 
	*/
	public void openAddItemDialog(String initSurName) {
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
		// Освобождаем ресурсы
		flDialog.dispose();
		flDialog = null;
	}


	public void openEditItemDialog(int index) {
		FL fl = model.getAllFLs().get(index);
		FLDialog flDialog = new FLDialog(dialog, fl);
		flDialog.setVisible(true);
		
		if (flDialog.getResult() == FLDialog.OK) {
			model.updateFL(fl);
			dialog.updatedFL(index);
		}

		// Освобождаем ресурсы
		flDialog.dispose();
		flDialog = null;

	}

	public void removeItem(int index) {
		FL fl = model.getAllFLs().get(index);
		model.deleteFL(fl);
		dialog.removedFL(index);
	}

}