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

import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersModel;

import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialogFactory;

import org.kesler.simplereg.gui.reestr.export.ReestrExporter;


public class ReestrViewController implements ReceptionsModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;

	private ReceptionsModel model;
    private ReceptionsFiltersModel filtersModel;
//	private List<ReceptionsFilter> filters;

	private ProcessDialog processDialog = null;

	private boolean listenReceptionsModel = true;

	public static synchronized ReestrViewController getInstance() {
		if (instance == null) {
			instance = new ReestrViewController();
		}
		return instance;
	}

	private ReestrViewController() {
		model = new ReceptionsModel();
		model.addReceptionsModelStateListener(this);

        filtersModel = model.getFiltersModel();
//		filters = new ArrayList<ReceptionsFilter>();
		// создаем вид с привязкой к этому контроллеру
		
	}

	// Открывает основной вид
	public void openView(JFrame parentFrame) {
		view = new ReestrView(this, parentFrame);
		view.setVisible(true);
	}

	public List<ReceptionsFilter> getFilters() {
		return filtersModel.getFilters();
	}

	// добавление фильра - вызывается из вида
	public void addFilter(ReceptionsFiltersEnum filterEnum) {
		
		// Создаем подходящий диалог
		ReceptionsFilterDialog receptionsFilterDialog = ReceptionsFilterDialogFactory.createDialog(view, filterEnum);
		if (receptionsFilterDialog == null) return;

		/// Открываем диалог
		receptionsFilterDialog.setVisible(true);

		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			int index = filtersModel.addFilter(receptionsFilterDialog.getReceptionsFilter());
			view.getFilterListModel().filterAdded(index);
		}
	}


	public void searchByReceptionCode(String receptionCodeString) {
        filtersModel.setQuickFilter(QuickReceptionsFiltersEnum.RECEPTION_CODE, receptionCodeString);
	}

    public void searchByRosreestrCode(String rosreestrCodeString) {
        filtersModel.setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE, rosreestrCodeString);
    }

	// Редактирование фильтра - вызывается из вида
	public void editFilter(int filterIndex) {
		if (filterIndex == -1) {
			JOptionPane.showMessageDialog(view, "Фильтр не выбран", " Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		// Получаем выбранный фильтр
		ReceptionsFilter receptionsFilter = filtersModel.getFilters().get(filterIndex);

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

		filtersModel.removeFilter(index);
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
		model.applyFiltersInSeparateThread();

	}

	// Очищает все фильтры
	public void resetFilters() {
		int count = filtersModel.getFilters().size();
		filtersModel.resetFilters();
		view.getFilterListModel().filtersCleared(count);
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
        if (receptionDialog.getResult() == ReceptionDialog.OK) {
            model.updateReception(reception);
        }
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
		ReestrExporter.exportReestr(model.getFilteredReceptions());
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