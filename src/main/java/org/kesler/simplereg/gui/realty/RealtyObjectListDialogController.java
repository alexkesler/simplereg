package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelState;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelStateListener;


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
		dialog.setVisible(true);
		if (dialog.getResult() == RealtyObjectListDialog.OK) {
			realtyObject = dialog.getSelectedRealtyObject();
		}

		return realtyObject;
	}

	public void readRealtyObjects() {
		Thread realtyListReaderThread = new Thread(new RealtyListReader());

		processDialog = new ProcessDialog(dialog, "Работаю", "Читаю список объектов недвижимости");
		realtyListReaderThread.start();	

		processDialog.setVisible(true);

		if (processDialog.getResult() == ProcessDialog.ERROR) {
			JOptionPane.showMessageDialog(dialog, "Ошибка чтения списка объектов недвижимости", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		processDialog.dispose();
		processDialog = null;

	}

	public List<RealtyObject> getRealtyObjects() {

		return model.getFilteredRealtyObjects();

	}

	public void filterRealtyObjects(String filterString) {
		model.filterRealtyObjects(filterString);
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
	public void realtyObjectsModelStateChanged(RealtyObjectsModelState state) {
		if (processDialog == null) {
			return;
		}
		switch (state) {
			case CONNECTING:
				processDialog.setContent("Соединяюсь...");			
			break;
			case READING:
				processDialog.setContent("Читаю список объектов недвижимости");
			break;	
			case UPDATED:
				processDialog.setVisible(false);
			break;
			case ERROR:
				processDialog.setResult(ProcessDialog.ERROR);
				processDialog.setVisible(false);
			break;			
		}
	}


	class RealtyListReader implements Runnable {
		public void run() {
			model.readRealtyObjectsFromDB();
		}
	}

}