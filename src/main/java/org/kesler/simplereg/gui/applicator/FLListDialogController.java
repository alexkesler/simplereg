package org.kesler.simplereg.gui.applicator;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.applicator.FLService;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.gui.GenericListDialogController;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FLListDialogController implements GenericListDialogController<FL>{
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private FLService flService;
	private GenericListDialog<FL> dialog;
	private static FLListDialogController instance = null;


	public static synchronized FLListDialogController getInstance() {
		if (instance == null) {
			instance = new FLListDialogController();
		}
		return instance;
	}

	private FLListDialogController() {
		flService = FLService.getInstance();
	}

	/**
	* Открывает диалог просмотра-редактирования заявителя - физического лица
	*/
	public void openDialog(JFrame parentFrame) {
		log.info("Open dialog");
		dialog = new GenericListDialog<FL>(parentFrame, "Заявители", this, GenericListDialog.VIEW_FILTER_MODE);

		flService.setFilterString("");
		updateItems();

		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	} 

	/**
	* Открывает диалог заявителя - физического лица
	*/
	public void openDialog(JDialog parentDialog) {
		log.info("Open dialog");
		dialog = new GenericListDialog<FL>(parentDialog, "Заявители", this, GenericListDialog.VIEW_FILTER_MODE);

		flService.setFilterString("");
		updateItems();

		dialog.setVisible(true);
		
		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

	} 

	/**
	* Открывает диалог выбора заявителя - физического лица
	*/
	public FL openSelectDialog(JFrame parentFrame) {
		log.info("Open select dialog");
		FL selectedFL = null;
		// filterItems("");
		dialog = new GenericListDialog<FL>(parentFrame, "Выбор заявителя", this, GenericListDialog.SELECT_FILTER_MODE);

		flService.setFilterString("");
		updateItems();

		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			// получаем выбранное физ лицо
			selectedFL = dialog.getSelectedItem();
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return selectedFL;
	} 

	/**
	* Открывает диалог выбора заявителя - физического лица
	*/
	public FL openSelectDialog(JDialog parentDialog) {
		log.info("Open select dialog");
		FL selectedFL = null;
		dialog = new GenericListDialog<FL>(parentDialog, "Выбор заявителя", this, GenericListDialog.SELECT_FILTER_MODE);

		flService.setFilterString("");
		updateItems();
		dialog.setVisible(true);

		if (dialog.getResult() == GenericListDialog.OK) {
			// получаем выбранное физ лицо

			selectedFL = dialog.getSelectedItem();
			
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;

		return selectedFL;
	} 


	@Override
	public void updateItems() {
		FLListUpdateWorker flListUpdateWorker = new FLListUpdateWorker();
		dialog.showProcess();
		flListUpdateWorker.execute();
	}

	/**
	* Создает в модели фильтрованный список
	* @param filter определяет фильтр для записей. Если строка пустая - модель будет возвращать полный список.
	*/
	@Override
	public void filterItems(String filter) {
		FLListFilterWorker flListFilterWorker = new FLListFilterWorker(filter);
		dialog.showProcess();
		flListFilterWorker.execute();
	}


	/**
	* Открывает диалог добавления нового физического лица
	*/
	@Override
	public boolean openAddItemDialog() {
		boolean result = false;
		FLDialog flDialog = new FLDialog(dialog);
		flDialog.setVisible(true);
		if (flDialog.getResult() == FLDialog.OK) {
			// очищаем фильтр
			filterItems("");
			// запоминаем новое физ лицо		
			FL fl = flDialog.getFL();
			FLAddWorker flAddWorker = new FLAddWorker(fl);
			dialog.showProcess();
			flAddWorker.execute();
			result = true;
		}
		flDialog.dispose();

		return result;
	}

	/**
	* Открывает диалог добавления физического лица с введенной фамилией
	* @param initSurName строка, на основнии которой создается фамилия 
	*/

	public boolean openAddItemDialog(String initSurName) {
		boolean result = false;
		initSurName = initSurName.toLowerCase();
		String firstLetter = initSurName.substring(0,1);
		firstLetter = firstLetter.toUpperCase();
		initSurName = firstLetter + initSurName.substring(1, initSurName.length());
		FLDialog flDialog = new FLDialog(dialog, initSurName);
		flDialog.setVisible(true);
		if (flDialog.getResult() == FLDialog.OK) {
			FL fl = flDialog.getFL();
			FLAddWorker flAddWorker = new FLAddWorker(fl);
			dialog.showProcess();
			flAddWorker.execute();
			result = true;
		}
		// Освобождаем ресурсы
		flDialog.dispose();

		return result;
	}

	@Override
	public boolean openEditItemDialog(FL fl) {
		boolean result = false;
		FLDialog flDialog = new FLDialog(dialog, fl);
		flDialog.setVisible(true);
		
		if (flDialog.getResult() == FLDialog.OK) {
			FLUpdateWorker flUpdateWorker = new FLUpdateWorker(fl);
			dialog.showProcess();
			flUpdateWorker.execute();
			result = true;
		}

		// Освобождаем ресурсы
		flDialog.dispose();

		return result;

	}

	@Override
	public boolean removeItem(FL fl) {
		FLRemoveWorker flRemoveWorker = new FLRemoveWorker(fl);
		dialog.showProcess();
		flRemoveWorker.execute();
		return true;
	}


	class FLListUpdateWorker extends SwingWorker<List<FL>, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		@Override
		protected List<FL> doInBackground() throws Exception {
			log.info("Read FLs");
			flService.readLFs();
			log.info("Filter FLs");
			flService.filterFLs();
			return flService.getFilteredFLs();
		}

		@Override
		protected void done() {
			try {
				dialog.hideProcess();
				List<FL> fls = get();
				dialog.setItems(fls);
				log.info("Reading FLs complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error reading FLs",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при чтении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class FLListFilterWorker extends SwingWorker<List<FL>, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());

		public FLListFilterWorker(String filter) {
			flService.setFilterString(filter);
		}

		@Override
		protected List<FL> doInBackground() throws Exception {
			log.info("Filter FLs");
			flService.filterFLs();
			return flService.getFilteredFLs();
		}

		@Override
		protected void done() {
			try {
				dialog.hideProcess();
				List<FL> fls = get();
				dialog.setItems(fls);
				log.info("Filtering FLs complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error filtering FLs",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при фильтрации данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class FLAddWorker extends SwingWorker<Integer, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private FL fl;

		public FLAddWorker(FL fl) {
			this.fl = fl;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			log.info("Adding FL: " + fl.getFIO());
			return flService.addFL(fl);
		}

		@Override
		protected void done() {
			try {
				dialog.hideProcess();
				Integer index = get();
				dialog.addedItem(index);
				log.info("Adding FL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error adding FL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при сохранении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class FLUpdateWorker extends SwingWorker<Void, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private FL fl;

		public FLUpdateWorker(FL fl) {
			this.fl = fl;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Updating FL: "+ fl.getFIO());
			flService.updateFL(fl);
			return null;
		}

		@Override
		protected void done() {
			try {
				dialog.hideProcess();
				get();
				log.info("Updating FL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error updating FL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при обновлении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class FLRemoveWorker extends SwingWorker<Void, Void> {
		private Logger log = Logger.getLogger(this.getClass().getSimpleName());
		private FL fl;

		public FLRemoveWorker(FL fl) {
			this.fl = fl;
		}

		@Override
		protected Void doInBackground() throws Exception {
			log.info("Removing FL: "+ fl.getFIO());
			flService.removeFL(fl);
			return null;
		}

		@Override
		protected void done() {
			try {
				dialog.hideProcess();
				get();
				log.info("Removing FL complete");
			} catch (InterruptedException e) {
				log.error("Interrupted",e);
			} catch (ExecutionException e) {
				log.error("Error removing FL",e);
				JOptionPane.showMessageDialog(dialog,
						"Ошибка при удалении данных: "+e.getMessage(),
						"Ошибка",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}