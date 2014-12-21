package org.kesler.simplereg.gui.applicator;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.applicator.ULService;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;

public class ULListDialogController implements GenericListDialogController<UL> {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private static ULListDialogController instance = null;

	private GenericListDialog<UL> dialog;
	private ULService ulService;

	public static synchronized ULListDialogController getInstance() {
		if (instance == null) {
			instance = new ULListDialogController();
		}
		return instance;
	}

	private ULListDialogController() {
		ulService = ULService.getInstance();
	}

	public void openDialog(JDialog parentDialog) {
		log.info("Open dialog");
		dialog = new GenericListDialog<UL>(parentDialog, "Юр лица", this, GenericListDialog.VIEW_FILTER_MODE);

		ulService.setFilterString("");
		updateItems();
		dialog.setVisible(true);
		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	public void openDialog(JFrame parentFrame) {
		log.info("Open dialog");
		dialog = new GenericListDialog<UL>(parentFrame, "Юр лица", this, GenericListDialog.VIEW_FILTER_MODE);

		ulService.setFilterString("");
		updateItems();
		dialog.setVisible(true);

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	public UL openSelectDialog(JDialog parentDialog) {
		log.info("Open select dialog");
		dialog = new GenericListDialog<UL>(parentDialog, "Юр лица", this, GenericListDialog.SELECT_FILTER_MODE);

		ulService.setFilterString("");
		updateItems();

		dialog.setVisible(true);

		UL ul = null;
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			ul = ulService.getFilteredULs().get(selectedIndex);
		}

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
		return ul;
	}

	public UL openSelectDialog(JFrame parentFrame) {
		log.info("Open select dialog");
		dialog = new GenericListDialog<UL>(parentFrame, "Юр лица", this, GenericListDialog.SELECT_FILTER_MODE);

		ulService.setFilterString("");
		updateItems();

		dialog.setVisible(true);

		UL ul = null;
		if (dialog.getResult() == GenericListDialog.OK) {
			int selectedIndex = dialog.getSelectedIndex();
			ul = ulService.getFilteredULs().get(selectedIndex);
		}

		// освобождаем ресурсы
		dialog.dispose();
		dialog = null;
		return ul;

	}


	@Override
	public void updateItems() {
		log.info("Update ULs");
		ULListUpdateWorker ulListUpdateWorker = new ULListUpdateWorker();
		dialog.showProcess();
		ulListUpdateWorker.execute();
	}

	@Override
	public void filterItems(String filterString) {
		log.info("Filter ULs");
		ULListFilterWorker ulListFilterWorker = new ULListFilterWorker(filterString);
		dialog.showProcess();
		ulListFilterWorker.execute();
	}

	@Override
	public boolean openAddItemDialog() {
		log.info("Open add UL dialog");
		boolean result = false;
		ULDialog ulDialog = new ULDialog(dialog);
		ulDialog.setVisible(true);
		if (ulDialog.getResult() == ULDialog.OK) {
			filterItems("");
			UL ul = ulDialog.getUL();
			ULAddWorker ulAddWorker = new ULAddWorker(ul);
			dialog.showProcess();
			ulAddWorker.execute();
			result = true;
		}
		// Освобождаем ресурсы
		ulDialog.dispose();
		ulDialog = null;

		return result;
	}

	@Override
	public boolean openEditItemDialog(UL ul) {
		log.info("Open edit UL dialog");
		boolean result = false;

		ULDialog ulDialog = new ULDialog(dialog, ul);
		ulDialog.setVisible(true);
		
		if (ulDialog.getResult() == ULDialog.OK) {
			ULUpdateWorker ulUpdateWorker = new ULUpdateWorker(ul);
			dialog.showProcess();
			ulUpdateWorker.execute();

			result = true;
		}
		// Освобождаем ресурсы
		ulDialog.dispose();
		ulDialog = null;

		return result;
	}

	@Override
	public boolean removeItem(UL ul) {
		log.info("Remove UL");

		ULRemoveWorker ulRemoveWorker = new ULRemoveWorker(ul);
		dialog.showProcess();
		ulRemoveWorker.execute();

		return true;
	}

	class ULListUpdateWorker extends SwingWorker<List<UL>, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		@Override
		protected List<UL> doInBackground() throws Exception {
			log.info("Reading ULs...");
			ulService.readULs();
			log.info("Filtering ULs");
			ulService.filterULs();
			List<UL> uls = ulService.getFilteredULs();
			return uls;
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				List<UL> uls = get();
				dialog.setItems(uls);
				log.info("Update ULs complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error reading ULs",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при чтении данных: " + e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ULListFilterWorker extends SwingWorker<List<UL>, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());

		public ULListFilterWorker(String filter) {
			ulService.setFilterString(filter);
		}

		@Override
		protected List<UL> doInBackground() throws Exception {
			log.info("Filter ULs");
			ulService.filterULs();
			return ulService.getFilteredULs();
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				List<UL> uls = get();
				dialog.setItems(uls);
				log.info("Filtering ULs complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error filtering ULs",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при фильтрации данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ULAddWorker extends SwingWorker<Integer, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private UL ul;

		public ULAddWorker(UL ul) {
			this.ul = ul;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			log.info("Adding UL: " + ul.getShortName());
			return ulService.addUL(ul);
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				Integer index = get();
				dialog.addedItem(index);
				log.info("Adding UL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error adding UL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при сохранении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ULUpdateWorker extends SwingWorker<Void, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private UL ul;

		public ULUpdateWorker(UL ul) {
			this.ul = ul;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Updating UL: " + ul.getShortName());
			ulService.updateUL(ul);
			return null;
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				get();
				log.info("Updating UL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error updating UL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при обновлении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class ULRemoveWorker extends SwingWorker<Void, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private UL ul;

		public ULRemoveWorker(UL ul) {
			this.ul = ul;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Removing UL: " + ul.getShortName());
			ulService.removeUL(ul);
			return null;
		}

		@Override
		protected void done() {
			dialog.hideProcess();
			try {
				get();
				log.info("Removing UL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error removing UL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при удалении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}


}