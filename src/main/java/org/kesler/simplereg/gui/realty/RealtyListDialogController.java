package org.kesler.simplereg.gui.realty;

import java.util.List;

import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;

import org.kesler.simplereg.gui.util.ProcessDialog;


public class RealtyListDialogController {

	private RealtyListDialog dialog;

	private static RealtyListDialogController instance = null;

	private RealtyListDialogController() {
		
	}

	public static synchronized RealtyListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyListDialogController();
		}
		return instance;
	}


	public void showDialog() {
		dialog.setVisible(true);
	}

	public List<RealtyObject> getAllRealtyObjects() {
		
		Thread realtyListReaderThread = new Thread(new RealtyListReader());

		ProcessDialog processDialog = new ProcessDialog(dialog, "Работаю", "Читаю список объектов недвижимости");
		realtyListReaderThread.start();	

		processDialog.setVisible(true);


		return RealtyObjectsModel.getInstance().getAllRealtyObjects();


	}



	class RealtyListReader implements Runnable {
		public void run() {
			RealtyObjectsModel.getInstance().readRealtyObjectsFromDB();
		}
	}

}