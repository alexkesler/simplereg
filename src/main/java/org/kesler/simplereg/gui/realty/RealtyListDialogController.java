package org.kesler.simplereg.gui.realty;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelState;
import org.kesler.simplereg.logic.realty.RealtyObjectsModelStateListener;


import org.kesler.simplereg.gui.util.ProcessDialog;


public class RealtyListDialogController implements RealtyObjectsModelStateListener {

	private RealtyListDialog dialog;
	private RealtyObjectsModel model;

	private ProcessDialog processDialog;

	private static RealtyListDialogController instance = null;

	private RealtyListDialogController() {
		model = RealtyObjectsModel.getInstance();
		model.addRealtyObjectsModelStateListener(this);
	}

	public static synchronized RealtyListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyListDialogController();
		}
		return instance;
	}


	public RealtyObject showDialog(JFrame parentFrame) {
		RealtyObject realtyObject = null;

		dialog = new RealtyListDialog(this, parentFrame);
		dialog.setVisible(true);
		if (dialog.getResult() == RealtyListDialog.OK) {
			realtyObject = dialog.getSelectedRealtyObject();
		}

		return realtyObject;
	}

	public List<RealtyObject> getAllRealtyObjects() {

		
		Thread realtyListReaderThread = new Thread(new RealtyListReader());

		processDialog = new ProcessDialog(dialog, "Работаю", "Читаю список объектов недвижимости");
		realtyListReaderThread.start();	

		processDialog.setVisible(true);

		if (processDialog.getResult() == ProcessDialog.ERROR) {
			JOptionPane.showMessageDialog(dialog, "Ошибка чтения списка объектов недвижимости", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}


		return RealtyObjectsModel.getInstance().getAllRealtyObjects();


	}

	public void addRealtyObject() {

		RealtyDialog realtyDialog = new RealtyDialog(dialog);
		realtyDialog.setVisible(true);
		if (realtyDialog.getResult() == RealtyDialog.OK) {
			RealtyObject realtyObject = realtyDialog.getRealtyObject();
			model.addRealtyObject(realtyObject);
			dialog.realtyObjectAdded(model.getAllRealtyObjects().size()-1);
		}

	}

	public void editRealtyObject(int index) {

		RealtyObject realtyObject = model.getAllRealtyObjects().get(index);

		RealtyDialog realtyDialog = new RealtyDialog(dialog, realtyObject);

		if (realtyDialog.getResult() == RealtyDialog.OK) {
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