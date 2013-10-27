package org.kesler.simplereg.gui.reestr;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import org.kesler.simplereg.gui.util.ProcessDialog;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.ReceptionsModelState;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.gui.reception.ReceptionDialog;

import org.kesler.simplereg.gui.reestr.column.ReestrColumnsDialog;

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.ByRecordReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.StatusReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.ServiceReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.OperatorReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.ToIssueDateReceptionsFilter;

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ByRecordReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.StatusReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ServiceReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.OperatorReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ToIssueDateReceptionsFilterDialog;

import org.kesler.simplereg.gui.reestr.print.ReestrExporter;


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

	// Открывает основной вид
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
		/// Создаем диалог на основнии сведений о необходимом типе диалога
		switch (filter) {
			case OPEN_DATE:
				receptionsFilterDialog = new OpenDateReceptionsFilterDialog(view);
			break;
			case BY_RECORD:
				receptionsFilterDialog = new ByRecordReceptionsFilterDialog(view);
			break;
			case STATUS:
				receptionsFilterDialog = new StatusReceptionsFilterDialog(view);	
			break;
			case SERVICE:
				receptionsFilterDialog = new ServiceReceptionsFilterDialog(view);	
			break;
			case OPERATOR:
				receptionsFilterDialog = new OperatorReceptionsFilterDialog(view);	
			break;
			case TO_ISSUE_DATE:
				receptionsFilterDialog = new ToIssueDateReceptionsFilterDialog(view);	
			break;
			default:
				return;
		}
		
		/// Дальнейшие действия одинаковы для всех диалогов
		receptionsFilterDialog.setVisible(true);
		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			filters.add(receptionsFilterDialog.getReceptionsFilter());
			view.getFilterListModel().filterAdded(filters.size()-1);
		}
	}

	// Редактирование фильтра - вызывается из вида
	public void editFilter(int filterIndex) {
		if (filterIndex == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		ReceptionsFilterDialog receptionsFilterDialog = null;

		ReceptionsFilter receptionsFilter = filters.get(filterIndex);

		// По классу фильтра определяем какой диалог создавать
		if (receptionsFilter instanceof OpenDateReceptionsFilter) { // Фильтр по дате открытия

			OpenDateReceptionsFilter openDateReceptionsFilter = (OpenDateReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new OpenDateReceptionsFilterDialog(view, openDateReceptionsFilter);

		} else if (receptionsFilter instanceof ByRecordReceptionsFilter) { // Фильтр по предв записи

			ByRecordReceptionsFilter byRecordReceptionsFilter = (ByRecordReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new ByRecordReceptionsFilterDialog(view, byRecordReceptionsFilter);

		} else if (receptionsFilter instanceof StatusReceptionsFilter) { // Фильтр по состоянию

			StatusReceptionsFilter statusReceptionsFilter = (StatusReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new StatusReceptionsFilterDialog(view, statusReceptionsFilter);

		} else if (receptionsFilter instanceof ServiceReceptionsFilter) { // Фильтр по услуге

			ServiceReceptionsFilter serviceReceptionsFilter = (ServiceReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new ServiceReceptionsFilterDialog(view, serviceReceptionsFilter);

		} else if (receptionsFilter instanceof OperatorReceptionsFilter) { // Фильтр по оператору

			OperatorReceptionsFilter operatorReceptionsFilter = (OperatorReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new OperatorReceptionsFilterDialog(view, operatorReceptionsFilter);

		} else if (receptionsFilter instanceof ToIssueDateReceptionsFilter) { // Фильтр по дате не выдачу

			ToIssueDateReceptionsFilter toIssueDateReceptionsFilter = (ToIssueDateReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new ToIssueDateReceptionsFilterDialog(view, toIssueDateReceptionsFilter);

		} else return;

		// дальнейшие действия одинаковы для всех диалогов
		receptionsFilterDialog.setVisible(true);
		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			view.getFilterListModel().filterUpdated(filterIndex);
		}

	}

	public void removeFilter(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		filters.remove(index);
		view.getFilterListModel().filterRemoved(index);
	}

	// открывает диалог редактирования видимых колонок - вызывается из вида
	public void openColumnsDialog() {
		ReestrColumnsDialog reestrColumnsDialog  = new ReestrColumnsDialog(view);
		reestrColumnsDialog.setVisible(true);
	}

	// применяет созданный набор фильтров - вызывается из вида
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

		// В конце процедуры удаляем диалог
		processDialog.dispose();
		processDialog = null;

		// Зтем фильтруем записи в отдельном потоке
		processDialog = new ProcessDialog(view, "Работаю", "Фильтрую записи");
		Thread receptionsFiltererThread = new Thread(new ReceptionsFilterer());
		receptionsFiltererThread.start();
		processDialog.setVisible(true);

		// В конце процедуры удаляем диалог
		processDialog.dispose();
		processDialog = null;

	}

	// Очищает все фильтры
	public void resetFilters() {
		int count = filters.size();
		filters = new ArrayList<ReceptionsFilter>();
		view.getFilterListModel().filtersCleared(count);
	}


	public void openReceptionDialog(Reception reception) {
		ReceptionDialog receptionDialog = new ReceptionDialog(view, reception);
		receptionDialog.setVisible(true);
		
	}

	public void createXLSFromReestrTable() {
		ReestrExporter.exportReestr();
	}

	// класс для чтения данных о приемах в отдельном потоке
	private class ReceptionsReader implements Runnable {
		public void run() {
			model.readReceptionsFromDB();
		}
	}

	// класс для фильтрации в отдельном потоке
	private class ReceptionsFilterer implements Runnable {
		public void run() {
			model.applyFilters(filters);
		}
	}


	// реализует интерфейс для слушателя модели приемов 
	@Override
	public void receptionsModelStateChanged(ReceptionsModelState state) {
		if(processDialog == null) return; // Управление происходит через processDialog поэтому если не задан - выходим
		switch (state) {
			case CONNECTING:
				if(processDialog.isVisible()) {
					processDialog.setContent("Соединяюсь");
				}	
			break;
			case READING:
				if(processDialog.isVisible()) {
					processDialog.setContent("Читаю список приемов");
				}	
			break;
			case UPDATED:
				processDialog.setContent("Готово");
				processDialog.setVisible(false);
			break;
			case FILTERING:
				if(processDialog.isVisible()) {
					processDialog.setContent("Фильтрую список приемов");
				}	
			break;
			case FILTERED:
				processDialog.setContent("Готово");
				processDialog.setVisible(false);
			break;		
			case ERROR:
				processDialog.setResult(ProcessDialog.ERROR);
				processDialog.setContent("Ошибка");
				processDialog.setVisible(false);
			break;
		}					
	} 
}