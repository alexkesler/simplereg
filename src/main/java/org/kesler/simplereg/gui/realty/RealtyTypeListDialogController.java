package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;

import org.kesler.simplereg.logic.realty.RealtyType;
import org.kesler.simplereg.logic.realty.RealtyTypesModel;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController; 

public class RealtyTypeListDialogController implements GenericListDialogController<RealtyType> {
	
	private static RealtyTypeListDialogController instance;

	GenericListDialog<RealtyType> dialog;
	RealtyTypesModel model;

	public static synchronized RealtyTypeListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyTypeListDialogController();
		}
		return instance;
	} 

	private RealtyTypeListDialogController() {
		
		model = RealtyTypesModel.getInstance();
	}

	public RealtyType showDialog(JFrame parentFrame) {
		RealtyType realtyType = null;
		dialog = new GenericListDialog<RealtyType>(parentFrame, this);
		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			realtyType = model.getAllRealtyTypes().get(dialog.getSelectedIndex());
		}

		return realtyType;
	}

	public List<RealtyType> getAllItems() {
		return model.getAllRealtyTypes();
	}

	public void openAddItemDialog() {

		RealtyTypeDialog realtyTypeDialog = new RealtyTypeDialog(dialog);
		realtyTypeDialog.setVisible(true);

		if (realtyTypeDialog.getResult() == RealtyTypeDialog.OK) {
			RealtyType realtyType = realtyTypeDialog.getRealtyType();
			int index = model.addRealtyType(realtyType);
			dialog.addedItem(index);			
		}

		
	}

	public void openEditItemDialog(int index) {

		RealtyType realtyType = model.getAllRealtyTypes().get(index);

		RealtyTypeDialog realtyTypeDialog = new RealtyTypeDialog(dialog, realtyType);
		realtyTypeDialog.setVisible(true);

		if (realtyTypeDialog.getResult() == RealtyTypeDialog.OK) {
			model.updateRealtyType(realtyType);	
			dialog.updatedItem(index);		
		}
	}

	public void removeItem(int index) {

		RealtyType realtyType = model.getAllRealtyTypes().get(index);

		model.removeRealtyType(realtyType);	
		dialog.removedItem(index);		

	}

}