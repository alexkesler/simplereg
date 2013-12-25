package org.kesler.simplereg.gui.reestr;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.ModelState;
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
import org.kesler.simplereg.gui.reestr.filter.ResultInMFCReceptionsFilter;
import org.kesler.simplereg.gui.reestr.filter.FilialReceptionsFilter;

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialogFactory;

import org.kesler.simplereg.gui.reestr.print.ReestrExporter;


public class ReestrViewController implements ReceptionsModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;

	private ReceptionsModel model;
	private List<ReceptionsFilter> filters;

	private ProcessDialog processDialog = null;

	private boolean listenReceptionsModel = true;

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
		
	}

	// Открывает основной вид
	public void openView(JFrame parentFrame) {
		view = new ReestrView(this, parentFrame);
		view.setVisible(true);
	}

	public List<ReceptionsFilter> getFilters() {
		return filters;
	}

	// добавление фильра - вызывается из вида
	public void addFilter(ReceptionsFiltersEnum filterEnum) {
		
		
		ReceptionsFilterDialog receptionsFilterDialog = ReceptionsFilterDialogFactory.createDialog(view, filterEnum);
		if (receptionsFilterDialog == null) return;

		/// Дальнейшие действия одинаковы для всех диалогов
		receptionsFilterDialog.setVisible(true);
		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			filters.add(receptionsFilterDialog.getReceptionsFilter());
			view.getFilterListModel().filterAdded(filters.size()-1);
		}
	}


	public void addFilter(ReceptionsFilter filter) {
		Class<?> filterClass = filter.getClass();
		int index = -1;

		for (int i = 0; i < filters.size(); i++) {
			ReceptionsFilter f = filters.get(i);
			if (f.getClass().equals(filterClass)) {
				index = i;
			}						
		}

		if (index != -1) {
			filters.remove(index);
			filters.add(index, filter);
		} else {
			filters.add(filter);
		}

	}


	// Редактирование фильтра - вызывается из вида
	public void editFilter(int filterIndex) {
		if (filterIndex == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		// Получаем выбранный фильтр
		ReceptionsFilter receptionsFilter = filters.get(filterIndex);

		// Создаем диалог редактирования фильтра
		ReceptionsFilterDialog receptionsFilterDialog = ReceptionsFilterDialogFactory.createDialog(view, receptionsFilter);	

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

		listenReceptionsModel = true;
		processDialog = new ProcessDialog(view);
		model.applyFiltersInSeparateThread(filters);

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


	public void openReceptionDialog(int index) {
		if (index == -1) {
			new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
			return;
		}
		List<Reception> receptions = model.getFilteredReceptions();
		Reception reception = receptions.get(index);
		ReceptionDialog receptionDialog = new ReceptionDialog(view, reception);
		receptionDialog.setVisible(true);	
		view.tableDataChanged();	
	}


	public void changeReceptionsStatus(int[] indexes, ReceptionStatus status) {
		if (indexes.length == 0) {
			new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
			return;
		}

		List<Reception> receptions = model.getFilteredReceptions();
		String selectedReceptionsString = "";
		List<Reception> selectedReceptions = new ArrayList<Reception>();
		for (int i=0; i<indexes.length; i++) {
			Reception reception = receptions.get(indexes[i]);
			selectedReceptions.add(reception);	
			selectedReceptionsString += "<p>" + reception.getReceptionCode() + ";</p>";		
		}

		int confirmResult = JOptionPane.showConfirmDialog(view, "<html>Установить для запросов: " + 
														selectedReceptionsString + 
														" статус: " + status.getName() + " ?</html>", 
														"Сменить статус?", JOptionPane.YES_NO_OPTION);

		if (confirmResult == JOptionPane.OK_OPTION) {
			for (Reception reception: selectedReceptions) {
				reception.setStatus(status);
				model.updateReception(reception);
			}
			view.tableDataChanged();						
		}

	}


	public void removeReceptions(int[] indexes) {
		if (indexes.length == 0) {
			new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
			return;
		}
		
		List<Reception> receptions = model.getFilteredReceptions();
		String selectedReceptionsString = "";
		List<Reception> selectedReceptions = new ArrayList<Reception>();
		for (int i=0; i<indexes.length; i++) {
			Reception reception = receptions.get(indexes[i]);
			selectedReceptions.add(reception);
			selectedReceptionsString += "<p>" + reception.getReceptionCode() + ";</p>";				
		}

		/// сюда надо поместить уточняющий вопрос
		int confirmResult = JOptionPane.showConfirmDialog(view, "<html>Удалить запросы: " + 
														selectedReceptionsString + " ?</html>", 
														"Удалить запросы?", JOptionPane.YES_NO_OPTION);

		if (confirmResult == JOptionPane.OK_OPTION) {
			for (Reception reception: selectedReceptions) {
				model.removeReception(reception);
			}
			view.tableStructureChanged();
		}

	}

	public void createXLSFromReestrTable() {
		ReestrExporter.exportReestr();
	}


	// реализует интерфейс для слушателя модели приемов 
	@Override
	public void receptionsModelStateChanged(ModelState state) {
		switch (state) {
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь");
				break;

			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список приемов");
				break;

			case UPDATED:
				// Ничего не делаем - ждем выполнения фильтрации
				break;

			case FILTERING:
				if (processDialog != null) processDialog.showProcess("Фильтрую список приемов");
				break;

			case FILTERED:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				List<Reception> filteredReceptions = model.getFilteredReceptions();
				view.setReceptions(filteredReceptions);
				break;

			case READY:
				// ProcessDialog.hideProcess();
				break;	

			case ERROR:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(view, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
				break;	
			
		}					
	} 
}