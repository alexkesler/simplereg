package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.realty.RealtyType;
import org.kesler.simplereg.logic.realty.RealtyTypesService;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController; 

public class RealtyTypeListDialogController implements GenericListDialogController<RealtyType> {
	
	private static RealtyTypeListDialogController instance;

	GenericListDialog<RealtyType> dialog;
	RealtyTypesService realtyTypesService;

	public static synchronized RealtyTypeListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyTypeListDialogController();
		}
		return instance;
	} 

	private RealtyTypeListDialogController() {
		
		realtyTypesService = RealtyTypesService.getInstance();
	}

	public RealtyType showSelectDialog(JFrame parentFrame) {
		RealtyType realtyType = null;
		dialog = new GenericListDialog<RealtyType>(parentFrame, "Типы недвижимости", this);
		dialog.setItems(realtyTypesService.getAllRealtyTypes());
		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK && dialog.getSelectedIndex() != -1) {
			realtyType = realtyTypesService.getAllRealtyTypes().get(dialog.getSelectedIndex());
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return realtyType;
	}

	public void showDialog(JFrame parentFrame) {

		dialog = new GenericListDialog<RealtyType>(parentFrame, "Типы недвижимости", this);
		dialog.setItems(realtyTypesService.getAllRealtyTypes());
		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	}

	// @Override
	public List<RealtyType> getAllItems() {
		return realtyTypesService.getAllRealtyTypes();
	}

	@Override
	public void updateItems() {
		realtyTypesService.readRealtyTypesFromDB();
	}

	@Override
	public void filterItems(String filter) {
		
	}

	@Override
	public boolean openAddItemDialog() {
		boolean result = false;

		RealtyTypeDialog realtyTypeDialog = new RealtyTypeDialog(dialog);
		realtyTypeDialog.setVisible(true);

		if (realtyTypeDialog.getResult() == RealtyTypeDialog.OK) {
			RealtyType realtyType = realtyTypeDialog.getRealtyType();
			int index = realtyTypesService.addRealtyType(realtyType);
			dialog.addedItem(index);
			result = true;			
		}

		// Освобождаем ресурсы
		realtyTypeDialog.dispose();
		realtyTypeDialog = null;	

		return result;	
		
	}

	@Override
	public boolean openEditItemDialog(RealtyType realtyType) {
		boolean result = false;

		RealtyTypeDialog realtyTypeDialog = new RealtyTypeDialog(dialog, realtyType);
		realtyTypeDialog.setVisible(true);

		if (realtyTypeDialog.getResult() == RealtyTypeDialog.OK) {
			realtyTypesService.updateRealtyType(realtyType);
			result = true;
		}

		// Освобождаем ресурсы
		realtyTypeDialog.dispose();
		realtyTypeDialog = null;		

		return result;
	}

	@Override
	public boolean removeItem(RealtyType realtyType) {
		boolean result = false;
		if (realtyType==null) return result;
		int confirmResult = JOptionPane.showConfirmDialog(dialog, "Удалить тип: " + realtyType + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
		if (confirmResult == JOptionPane.OK_OPTION) {
			realtyTypesService.removeRealtyType(realtyType);
			result = true;
		}			

		return result;
	}

}