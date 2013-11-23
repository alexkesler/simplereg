package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelStateListener;
import org.kesler.simplereg.logic.ModelState;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;


public class RealtyObjectListDialogController implements RealtyObjectsModelStateListener {

	private RealtyObjectListDialog dialog;
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

		dialog = new RealtyObjectListDialog(this, parentFrame);
		updateRealtyObjects();
		dialog.setVisible(true);
		if (dialog.getResult() == RealtyObjectListDialog.OK) {
			realtyObject = dialog.getSelectedRealtyObject();
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

	public void filterRealtyObjects(String filterString) {
		model.filterRealtyObjects(filterString);
		List<RealtyObject> realtyObjects = model.getFilteredRealtyObjects();
		dialog.setRealtyObjects(realtyObjects);
	}

	public void updateAndFilterRealtyObjects(String filterString) {
		processDialog = new ProcessDialog(dialog);
		model.readAndFilterRealtyObjectsInSeparateThread(filterString);
	}

	public void addRealtyObject() {

		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			RealtyObject realtyObject = realtyObjectDialog.getRealtyObject();
			int index = model.addRealtyObject(realtyObject);
			dialog.realtyObjectAdded(index);
		}

	}

	public void editRealtyObject(int index) {

		RealtyObject realtyObject = model.getAllRealtyObjects().get(index);

		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog, realtyObject);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			model.updateRealtyObject(realtyObject);
			dialog.realtyObjectUpdated(index);			
		}

	}

	public void removeRealtyObject(int index) {

		RealtyObject realtyObject = model.getAllRealtyObjects().get(index);

		model.removeRealtyObject(realtyObject);
		dialog.realtyObjectRemoved(index);
	
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
				dialog.setRealtyObjects(realtyObjects);
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
			break;
			case ERROR:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
			break;			
		}
	}


}