package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelStateListener;
import org.kesler.simplereg.logic.ModelState;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;

import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController; 


public class RealtyObjectListDialogController implements GenericListDialogController, RealtyObjectsModelStateListener {

	private GenericListDialog dialog;
	private RealtyObjectsModel model;

	private ProcessDialog processDialog;

	private static RealtyObjectListDialogController instance = null;

	private RealtyObjectListDialogController() {
		model = RealtyObjectsModel.getInstance();
		model.addRealtyObjectsModelStateListener(this);
	}

	public static synchronized RealtyObjectListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyObjectListDialogController();
		}
		return instance;
	}


	public RealtyObject showDialog(JFrame parentFrame) {
		RealtyObject realtyObject = null;

		dialog = new GenericListDialog<RealtyObject>(parentFrame, this);
		updateRealtyObjects();
		dialog.setVisible(true);
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			realtyObject = model.getFilteredRealtyObjects().get(selectedIndex);
		}

		return realtyObject;
	}

	public RealtyObject showDialog(JDialog parentDialog) {
		RealtyObject realtyObject = null;

		dialog = new GenericListDialog<RealtyObject>(parentDialog, this);
		updateRealtyObjects();
		dialog.setVisible(true);
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			realtyObject = model.getFilteredRealtyObjects().get(selectedIndex);
		}

		return realtyObject;
	}

	public void updateRealtyObjects() {

		processDialog = new ProcessDialog(dialog);
		model.readRealtyObjectsInSeparateThread();	

	}

	public List<RealtyObject> getRealtyObjects() {

		return model.getFilteredRealtyObjects();

	}

	@Override
	public void readItems() {
		model.readRealtyObjects();
	}

	@Override
	public void filterItems(String filterString) {
		model.filterRealtyObjects(filterString);
		List<RealtyObject> realtyObjects = model.getFilteredRealtyObjects();
		dialog.setItems(realtyObjects);
	}

	public void updateAndFilterRealtyObjects(String filterString) {
		processDialog = new ProcessDialog(dialog);
		model.readAndFilterRealtyObjectsInSeparateThread(filterString);
	}

	@Override
	public boolean openAddItemDialog() {
		boolean result = false;

		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			RealtyObject realtyObject = realtyObjectDialog.getRealtyObject();
			int index = model.addRealtyObject(realtyObject);
			dialog.addedItem(index);
			result = true;
		}

		return result;

	}

	@Override
	public boolean openEditItemDialog(int index) {
		boolean result = false;

		RealtyObject realtyObject = model.getAllRealtyObjects().get(index);

		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog, realtyObject);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			model.updateRealtyObject(realtyObject);
			dialog.updatedItem(index);	
			result = true;		
		}

		return result;

	}

	@Override
	public boolean removeItem(int index) {

		RealtyObject realtyObject = model.getAllRealtyObjects().get(index);

		model.removeRealtyObject(realtyObject);
		dialog.removedItem(index);

		return true;
	
	}


	@Override
	public void realtyObjectsModelStateChanged(ModelState state) {

		switch (state) {
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь...");			
			break;
			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список объектов недвижимости");
			break;	
			case UPDATED:
				List<RealtyObject> realtyObjects = model.getAllRealtyObjects();
				dialog.setItems(realtyObjects);
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
			break;
			case ERROR:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
			break;			
		}
	}


}