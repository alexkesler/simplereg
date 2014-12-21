package org.kesler.simplereg.gui.realty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsService;

import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;


public class RealtyObjectListDialogController implements GenericListDialogController<RealtyObject> {

	private GenericListDialog<RealtyObject> dialog;
	private RealtyObjectsService realtyObjectsService;

	private final List<RealtyObject> realtyObjects = new ArrayList<RealtyObject>();

	private static RealtyObjectListDialogController instance = null;

	private RealtyObjectListDialogController() {
		realtyObjectsService = RealtyObjectsService.getInstance();
	}

	public static synchronized RealtyObjectListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyObjectListDialogController();
		}
		return instance;
	}


	public void showDialog(JFrame parentFrame) {

		dialog = new GenericListDialog<RealtyObject>(parentFrame, "Объекты недвижимости", this, GenericListDialog.VIEW_FILTER_MODE);
		dialog.setItems(realtyObjects);
		updateRealtyObjects();
		dialog.setVisible(true);
	}

	public void showDialog(JDialog parentDialog) {
		dialog = new GenericListDialog<RealtyObject>(parentDialog, "Объекты недвижимости", this, GenericListDialog.VIEW_FILTER_MODE);
		dialog.setItems(realtyObjects);
		updateRealtyObjects();
		dialog.setVisible(true);
	}


	public RealtyObject showSelectDialog(JFrame parentFrame) {
		RealtyObject realtyObject = null;

		dialog = new GenericListDialog<RealtyObject>(parentFrame, "Объекты недвижимости", this, GenericListDialog.SELECT_FILTER_MODE);
		dialog.setItems(realtyObjects);
		updateRealtyObjects();
		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			realtyObject = realtyObjectsService.getFilteredRealtyObjects().get(selectedIndex);
		}

		return realtyObject;
	}

	public RealtyObject showSelectDialog(JDialog parentDialog) {
		RealtyObject realtyObject = null;

		dialog = new GenericListDialog<RealtyObject>(parentDialog, "Объекты недвижимости", this, GenericListDialog.SELECT_FILTER_MODE);
		dialog.setItems(realtyObjects);
		updateRealtyObjects();
		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {

			realtyObject = dialog.getSelectedItem();
		}

		return realtyObject;
	}

	@Override
	public void updateItems() {
		updateRealtyObjects();
	}

	@Override
	public void filterItems(String filterString) {
		realtyObjectsService.filterRealtyObjects(filterString);
		realtyObjects.clear();
		realtyObjects.addAll(realtyObjectsService.getFilteredRealtyObjects());
		dialog.updatedItems();
	}


	@Override
	public boolean openAddItemDialog() {
		boolean result = false;

		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			RealtyObject realtyObject = realtyObjectDialog.getRealtyObject();
			AddRealtyObjectWorker addRealtyObjectWorker = new AddRealtyObjectWorker(realtyObject);
			dialog.showProcess();
			addRealtyObjectWorker.execute();
		}

		return result;

	}

	@Override
	public boolean openEditItemDialog(RealtyObject realtyObject) {
		boolean result = false;


		RealtyObjectDialog realtyObjectDialog = new RealtyObjectDialog(dialog, realtyObject);
		realtyObjectDialog.setVisible(true);

		if (realtyObjectDialog.getResult() == RealtyObjectDialog.OK) {
			UpdateRealtyObjectWorker updateRealtyObjectWorker
					= new UpdateRealtyObjectWorker(realtyObject);
			dialog.showProcess();
			updateRealtyObjectWorker.execute();
			result = true;
		}

		return result;

	}

	@Override
	public boolean removeItem(RealtyObject realtyObject) {

		RemoveRealtyObjectWorker removeRealtyObjectWorker = new
				RemoveRealtyObjectWorker(realtyObject);
		dialog.showProcess();
		removeRealtyObjectWorker.execute();


		return true;
	
	}


	private void updateRealtyObjects() {
		UpdateRealtyObjectsWorker updateRealtyObjectsWorker = new UpdateRealtyObjectsWorker();
		dialog.showProcess();
		updateRealtyObjectsWorker.execute();
	}



	class UpdateRealtyObjectsWorker extends SwingWorker<List<RealtyObject>, Void> {
		private final Logger log = Logger.getLogger(this.getClass());
		@Override
		protected List<RealtyObject> doInBackground() throws Exception {
			log.info("Updating RealtyObjects");
			realtyObjectsService.readRealtyObjects();
			return realtyObjectsService.getAllRealtyObjects();
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				realtyObjects.clear();
				List<RealtyObject> ro = get();
				realtyObjects.addAll(ro);
				dialog.updatedItems();
				log.info("Update complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error reading RealtyObjects",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при чтении объектов: " + e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	class AddRealtyObjectWorker extends SwingWorker<Void, Void> {
		private final Logger log = Logger.getLogger(this.getClass());
		private RealtyObject realtyObject;

		public AddRealtyObjectWorker(RealtyObject realtyObject) {
			this.realtyObject = realtyObject;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Add RealtyObject");
			realtyObjectsService.addRealtyObject(realtyObject);
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
				log.info("Adding complete");
			} catch (InterruptedException e) {
				log.error("Interrupted", e);
			} catch (ExecutionException e) {
				log.error("Error adding RealtyObject",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при записи объекта недвижимости: " + e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class UpdateRealtyObjectWorker extends SwingWorker<Void, Void> {
		private final Logger log = Logger.getLogger(this.getClass());
		private RealtyObject realtyObject;

		public UpdateRealtyObjectWorker(RealtyObject realtyObject) {
			this.realtyObject = realtyObject;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Update RealtyObject");
			realtyObjectsService.updateRealtyObject(realtyObject);
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
				log.info("Update complete");
			} catch (InterruptedException e) {
				log.error("Interrupted", e);
			} catch (ExecutionException e) {
				log.error("Error updating RealtyObjects", e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при записи объекта недвижимости: " + e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class RemoveRealtyObjectWorker extends SwingWorker<Void, Void> {
		private final Logger log = Logger.getLogger(this.getClass());
		private RealtyObject realtyObject;

		public RemoveRealtyObjectWorker(RealtyObject realtyObject) {
			this.realtyObject = realtyObject;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Remove RealtyObject");
			realtyObjectsService.removeRealtyObject(realtyObject);
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
				log.info("Remove complete");
			} catch (InterruptedException e) {
				log.error("Interrupted", e);
			} catch (ExecutionException e) {
				log.error("Error removing RealtyObjects", e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при удалении объекта недвижимости: " + e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


}