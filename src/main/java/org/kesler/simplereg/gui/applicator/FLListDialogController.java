package org.kesler.simplereg.gui.applicator;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JDialog;

import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.applicator.FLModel;
import org.kesler.simplereg.logic.applicator.FLModelStateListener;
import org.kesler.simplereg.logic.ModelState;

import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;



public class FLListDialogController implements GenericListDialogController, FLModelStateListener{
	
	private FLModel model;
	private GenericListDialog dialog;
	private static FLListDialogController instance = null;

	private ProcessDialog processDialog = null;

	public static synchronized FLListDialogController getInstance() {
		if (instance == null) {
			instance = new FLListDialogController();
		}
		return instance;
	}

	private FLListDialogController() {
		model = FLModel.getInstance();
		model.addFLModelStateListener(this);
	}

	/**
	* Открывает диалог просмотра-редактирования заявителя - физического лица
	*/
	public void openDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<FL>(parentFrame, "Заявители", this, GenericListDialog.VIEW_FILTER_MODE);

		processDialog = new ProcessDialog(dialog);
		model.setFilterString("");
		model.readFLsInSeparateThread();

		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	} 

	/**
	* Открывает диалог заявителя - физического лица
	*/
	public void openDialog(JDialog parentDialog) {
		dialog = new GenericListDialog<FL>(parentDialog, "Заявители", this, GenericListDialog.VIEW_FILTER_MODE);

		processDialog = new ProcessDialog(dialog);
		model.setFilterString("");
		model.readFLsInSeparateThread();

		dialog.setVisible(true);
		
		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	} 

	/**
	* Открывает диалог выбора заявителя - физического лица
	*/
	public FL openSelectDialog(JFrame parentFrame) {
		FL selectedFL = null;
		// filterItems("");
		dialog = new GenericListDialog<FL>(parentFrame, "Выбор заявителя", this, GenericListDialog.SELECT_FILTER_MODE);

		processDialog = new ProcessDialog(dialog);
		model.setFilterString("");
		model.readFLsInSeparateThread();

		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			// получаем выбранное физ лицо
			int selectedIndex = dialog.getSelectedIndex();
			selectedFL = model.getFilteredFLs().get(selectedIndex);
			
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return selectedFL;
	} 

	/**
	* Открывает диалог выбора заявителя - физического лица
	*/
	public FL openSelectDialog(JDialog parentDialog) {
		FL selectedFL = null;
		// filterItems("");
		dialog = new GenericListDialog<FL>(parentDialog, "Выбор заявителя", this, GenericListDialog.SELECT_FILTER_MODE);

		processDialog = new ProcessDialog(dialog);
		model.setFilterString("");
		model.readFLsInSeparateThread();

		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			// получаем выбранное физ лицо
			int selectedIndex = dialog.getSelectedIndex();
			selectedFL = model.getFilteredFLs().get(selectedIndex);
			
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return selectedFL;
	} 


	@Override
	public void readItems() {
		processDialog = new ProcessDialog(dialog);
		model.readFLsInSeparateThread();
	}

	/**
	* Создает в модели фильтрованный список
	* @param filter определяет фильтр для записей. Если строка пустая - модель будет возвращать полный список.
	*/
	@Override
	public void filterItems(String filter) {
		processDialog = new ProcessDialog(dialog);
		model.setFilterString(filter);
		model.filterFLsInSeparateThread();
	}


	/**
	* Открывает диалог добавления нового физического лица
	*/
	@Override
	public boolean openAddItemDialog() {
		boolean result = false;
		FLDialog flDialog = new FLDialog(dialog);
		flDialog.setVisible(true);
		if (flDialog.getResult() == FLDialog.OK) {
			// очищаем фильтр
			filterItems("");
			// запоминаем новое физ лицо		
			FL fl = flDialog.getFL();
			int index = model.addFL(fl);
			if (index != -1) {
				dialog.addedItem(index);
				result = true;
			}	
		}
		flDialog.dispose();
		flDialog = null;

		return result;
	}

	/**
	* Открывает диалог добавления физического лица с введенной фамилией
	* @param initSurName строка, на основнии которой создается фамилия 
	*/
	public boolean openAddItemDialog(String initSurName) {
		boolean result = false;
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
				dialog.addedItem(index);
				result = true;
			}			
		}
		// Освобождаем ресурсы
		flDialog.dispose();
		flDialog = null;

		return result;
	}


	public boolean openEditItemDialog(int index) {
		boolean result = false;
		FL fl = model.getAllFLs().get(index);
		FLDialog flDialog = new FLDialog(dialog, fl);
		flDialog.setVisible(true);
		
		if (flDialog.getResult() == FLDialog.OK) {
			model.updateFL(fl);
			dialog.updatedItem(index);
			result = true;
		}

		// Освобождаем ресурсы
		flDialog.dispose();
		flDialog = null;

		return result;

	}

	public boolean removeItem(int index) {
		FL fl = model.getAllFLs().get(index);
		model.deleteFL(fl);
		dialog.removedItem(index);

		return true;
	}

	@Override 
	public void flModelStateChanged(ModelState state) {
		switch (state) {
			
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь...");			
				break;

			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список заявителей");
				break;	
			
			case WRITING:
				if (processDialog != null) processDialog.showProcess("Сохраняю изменения");
				break;	
			
			case UPDATED:
				// if (dialog != null) dialog.setItems(model.getAllOperators());
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();	
				break;

			case FILTERED:
				if (dialog != null) dialog.setItems(model.getFilteredFLs());
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				// new InfoDialog(dialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();	
				break;

			
			case READY:
				if (processDialog != null) {processDialog.hideProcess(); processDialog=null;}	
				break;
			
			case ERROR:				
				if (processDialog != null) {processDialog.hideProcess(); processDialog=null;}
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
				break;

		}
	}

}