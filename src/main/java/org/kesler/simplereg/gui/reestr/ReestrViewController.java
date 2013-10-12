package org.kesler.simplereg.gui.reestr;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.kesler.simplereg.gui.util.ProcessDialog;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelState;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;

import org.kesler.simplereg.gui.reestr.column.ReestrColumnsDialog;

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.StatusReceptionsFilter;

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.StatusReceptionsFilterDialog;


public class ReestrViewController implements ReceptionsModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;

	private ReceptionsModel model;
	private List<ReceptionsFilter> filters;

	private ProcessDialog processDialog;


	public static synchronized ReestrViewController getInstance() {
		if (instance == null) {
			instance = new ReestrViewController();
		}
		return instance;
	}

	private ReestrViewController() {
		model = ReceptionsModel.getInstance();
		model.addReceptionsModelStateListener(this);
		filters = new ArrayList<ReceptionsFilter>();
		// создаем вид с привязкой к этому контроллеру
		view = new ReestrView(this);
		
	}

	public void openView() {
		view.setVisible(true);
	}

	List<Reception> getReceptions() {
		return model.getAllReceptions();
	}

	public List<ReceptionsFilter> getFilters() {
		return filters;
	}

	List<Reception> getFilteredReceptions() {
		return model.getFilteredReceptions();
	}

	// добавление фильра - вызывается из вида
	public void addFilter(ReceptionsFiltersEnum filter) {
		ReceptionsFilterDialog receptionsFilterDialog = null;
		switch (filter) {
			case OPEN_DATE:
				receptionsFilterDialog = new OpenDateReceptionsFilterDialog(view);
			break;
			case STATUS:
				receptionsFilterDialog = new StatusReceptionsFilterDialog(view);	
			break;
			default:
				return;
		}
		

		receptionsFilterDialog.setVisible(true);
		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			filters.add(receptionsFilterDialog.getReceptionsFilter());
			view.getFilterListModel().filterAdded(filters.size()-1);
		}
	}

	// Редактирование фильта - вызывается из вида
	public void editFilter(int filterIndex) {
		if (filterIndex == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		ReceptionsFilterDialog receptionsFilterDialog = null;

		ReceptionsFilter receptionsFilter = filters.get(filterIndex);

		// По классу фильтра определяем какой диалог создавать
		if (receptionsFilter instanceof OpenDateReceptionsFilter) {
			OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new OpenDateReceptionsFilterDialog(view, openDateReceptionsFilter);
		} else if (receptionsFilter instanceof StatusReceptionsFilter) {
			StatusReceptionsFilter statusReceptionsFilter = (StatusReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new StatusReceptionsFilterDialog(view, statusReceptionsFilter);
		} else return;

		receptionsFilterDialog.setVisible(true);
		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			view.getFilterListModel().filterUpdated(filterIndex);
		}

	}

	public void openColumnsDialog() {
		ReestrColumnsDialog reestrColumnsDialog  = new ReestrColumnsDialog(view);
		reestrColumnsDialog.setVisible(true);
	}

	public void applyFilters() {

		// сначала читаем последние сведения из базы данных в отдельном потоке
		processDialog = new ProcessDialog(view, "Работаю", "Читаю список приемов");
		Thread receptionsReaderThread = new Thread(new ReceptionsReader());
		receptionsReaderThread.start();
		processDialog.setVisible(true); // открываем модальный диалог, ожидаем его закрытия

		if(processDialog.getResult() == ProcessDialog.ERROR) {
			JOptionPane.showMessageDialog(view, "Ошибка чтения из базы данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		// Зтем фильтруем записи в отдельном потоке
		processDialog = new ProcessDialog(view, "Работаю", "Фильтрую записи");
		Thread receptionsFiltererThread = new Thread(new ReceptionsFilterer());
		receptionsFiltererThread.start();
		processDialog.setVisible(true);

	}

	public void resetFilters() {
		int count = filters.size();
		filters = new ArrayList<ReceptionsFilter>();
		view.getFilterListModel().filtersCleared(count);
	}

	private class ReceptionsReader implements Runnable {
		public void run() {
			model.readReceptionsFromDB();
		}
	}

	private class ReceptionsFilterer implements Runnable {
		public void run() {
			model.applyFilters(filters);
		}
	}

	@Override
	public void receptionsModelStateChanged(ReceptionsModelState state) {
		if(processDialog == null) return; // Управление происходит через processDialog поэтому если не задан - выходим
		switch (state) {
			case CONNECTING:
				processDialog.setContent("Соединяюсь");
			break;
			case READING:
				processDialog.setContent("Читаю список приемов");
			break;
			case UPDATED:
				processDialog.setContent("Готово");
				processDialog.setVisible(false);
			break;
			case FILTERING:
				processDialog.setContent("Фильтрую список приемов");
			break;
			case FILTERED:
				processDialog.setContent("Готово");
				processDialog.setVisible(false);
			break;		
			case ERROR:
				processDialog.setResult(ProcessDialog.ERROR);
				processDialog.setVisible(false);
			break;
		}					
	} 
}