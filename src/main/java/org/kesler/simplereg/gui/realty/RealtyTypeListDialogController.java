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

	public RealtyType openDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<RealtyType>(parentFrame, this);
		dialog.setVisible(true);
	}

	public List<RealtyType> getAllItems() {
		return model.getAllRealtyTypes();
	}

	public void openAddItemDialog() {

		RealtyTypeDialog realtyTypeDialog = new RealtyTypeDialog();
		realtyTypeDialog.setVisible(true);

		if (realtyTypeDialog.getResult() == RealtyTypeDialog.OK) {
			RealtyType realtyType = realtyTypeDialog.getRealtyType();
			model.addRealtyType(realtyType);			
		}

		
	}

	public void openEditItemDialog(int index) {

	}

	public void removeItem(int index) {

	}

}