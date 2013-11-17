package org.kesler.simplereg.gui.reestr;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;

import org.kesler.simplereg.logic.reception.Reception;
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

import org.kesler.simplereg.gui.reestr.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.OpenDateReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ByRecordReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.StatusReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ServiceReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.OperatorReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ToIssueDateReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ResultInMFCReceptionsFilterDialog;

import org.kesler.simplereg.gui.reestr.print.ReestrExporter;


public class ReestrViewController implements ReceptionsModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;

	private ReceptionsModel model;
	private List<ReceptionsFilter> filters;

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
			case RESULT_IN_MFC:
				receptionsFilterDialog = new ResultInMFCReceptionsFilterDialog(view);	
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

		} else if (receptionsFilter instanceof ResultInMFCReceptionsFilter) { // Фильтр по дате не выдачу

			ResultInMFCReceptionsFilter resultInMFCReceptionsFilter = (ResultInMFCReceptionsFilter) receptionsFilter;
			receptionsFilterDialog = new ResultInMFCReceptionsFilterDialog(view, resultInMFCReceptionsFilter);

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
		// if(processDialog == null) return; // Управление происходит через processDialog поэтому если не задан - выходим
		switch (state) {
			case CONNECTING:
				ProcessDialog.showProcess(view, "Соединяюсь");
				// if(processDialog.isVisible()) {
				// 	processDialog.setContent("Соединяюсь");
				// }	
			break;
			case READING:
				ProcessDialog.showProcess(view, "Читаю список приемов");
				// if(processDialog.isVisible()) {
				// 	processDialog.setContent("Читаю список приемов");
				// }	
			break;
			case UPDATED:
				ProcessDialog.hideProcess();
				view.setReceptions(getFilteredReceptions());
				// processDialog.setContent("Готово");
				// processDialog.setVisible(false);
			break;
			case FILTERING:
				ProcessDialog.showProcess(view, "Фильтрую список приемов");

				// if(processDialog.isVisible()) {
				// 	processDialog.setContent("Фильтрую список приемов");
				// }	
			break;
			case READY:
				ProcessDialog.hideProcess();
				// processDialog.setContent("Готово");
				// processDialog.setVisible(false);
			break;		
			case ERROR:

				// processDialog.setResult(ProcessDialog.ERROR);
				// processDialog.setContent("Ошибка");
				// processDialog.setVisible(false);
			break;
		}					
	} 
}