package org.kesler.simplereg.gui.reestr;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.kesler.simplereg.export.reestr.ReestrExporter;
import org.kesler.simplereg.gui.AbstractDialog;
import org.kesler.simplereg.gui.reception.ReceptionDialogController;
import org.kesler.simplereg.gui.reception.select.SelectReceptionDialogController;
import org.kesler.simplereg.export.reestr.ReestrExporterFactory;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.ServiceState;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;

import org.kesler.simplereg.gui.reestr.column.ReestrColumnsDialog;

import org.kesler.simplereg.logic.reception.filter.ReceptionsFilter;
import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersModel;

import org.kesler.simplereg.logic.reception.filter.ReceptionsFiltersEnum;
import org.kesler.simplereg.logic.reception.filter.QuickReceptionsFiltersEnum;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialog;
import org.kesler.simplereg.gui.reestr.filter.ReceptionsFilterDialogFactory;


public class ReestrViewController implements ReceptionsModelStateListener{

	private static ReestrViewController instance = null;	
	private ReestrView view;
	private ChangeStatusDialog changeStatusDialog;
    private Logger log;

	private ReceptionsModel model;
    private ReceptionsFiltersModel filtersModel;

	private ProcessDialog processDialog = null;

	private boolean listenReceptionsModel = true;

	public static synchronized ReestrViewController getInstance() {
		if (instance == null) {
			instance = new ReestrViewController();
		}
		return instance;
	}

	private ReestrViewController() {
        log = Logger.getLogger(getClass().getSimpleName());
		model = new ReceptionsModel();
		model.addReceptionsModelStateListener(this);

        filtersModel = model.getFiltersModel();
	}

	// Открывает основной вид
	public void openView(JFrame parentFrame) {
		log.info("Opening new ReestrView");
        view = new ReestrView(this, parentFrame);
        changeStatusDialog = new ChangeStatusDialog(view);
		view.setVisible(true);
	}

	public List<ReceptionsFilter> getFilters() {
		return filtersModel.getFilters();
	}

	// добавление фильра - вызывается из вида
	public void addFilter(ReceptionsFiltersEnum filterEnum) {
		log.info("Open adding filter dialog " + filterEnum.toString());
		// Создаем подходящий диалог
		ReceptionsFilterDialog receptionsFilterDialog = ReceptionsFilterDialogFactory.createDialog(view, filterEnum);
		if (receptionsFilterDialog == null) return;

		/// Открываем диалог
		receptionsFilterDialog.setVisible(true);

		if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
			int index = filtersModel.addFilter(receptionsFilterDialog.getReceptionsFilter());
			view.getFilterListModel().filterAdded(index);
            log.info("Filter "+filterEnum.toString() +" added");
		}
	}

    public void setFromOpenDate(Date fromOpenDate) {
        filtersModel.setFromOpenDate(fromOpenDate);
    }

    public void setToOpenDate(Date toOpenDate) {
        filtersModel.setToOpenDate(toOpenDate);
    }

    public Date getFromOpenDate() {
        return filtersModel.getFromOpenDate();
    }

    public Date getToOpenDate() {
        return filtersModel.getToOpenDate();
    }

    public void searchByReceptionCode(String receptionCodeString) {
        if (!receptionCodeString.isEmpty()) {
            int index = filtersModel.setQuickFilter(QuickReceptionsFiltersEnum.RECEPTION_CODE, receptionCodeString);
            view.getFilterListModel().filterAdded(index);
        } else {
            int index = filtersModel.resetQuickFilter(QuickReceptionsFiltersEnum.RECEPTION_CODE);
            if (index != -1) view.getFilterListModel().filterRemoved(index);
        }
        applyFilters();
	}

    public void searchByRosreestrCode(String rosreestrCodeString) {
        if (!rosreestrCodeString.isEmpty()) {
            int index = filtersModel.setQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE, rosreestrCodeString);
            view.getFilterListModel().filterAdded(index);
        } else {
            int index = filtersModel.resetQuickFilter(QuickReceptionsFiltersEnum.ROSREESTR_CODE);
            if (index != -1) view.getFilterListModel().filterRemoved(index);
        }

        applyFilters();
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

		if(receptionsFilterDialog!=null) {
            receptionsFilterDialog.setVisible(true);
            if (receptionsFilterDialog.getResult() == ReceptionsFilterDialog.OK) {
                view.getFilterListModel().filterUpdated(filterIndex);
            }

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

    public void readFromDBAndApplyFilters() {
        processDialog = new ProcessDialog(view);
        model.readReceptionsAndApplyFiltersInSeparateThread();
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
        if (ReceptionDialogController.getInstance().showDialog(view,reception)) {
            view.tableDataChanged();
        }
	}


	public void changeReceptionsStatus(int[] indexes, ReceptionStatus status) {
		if (indexes.length == 0) {
			new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
			return;
		}

		List<Reception> receptions = model.getFilteredReceptions();
		List<Reception> selectedReceptions = new ArrayList<Reception>();
		for (int i=0; i<indexes.length; i++) {
			Reception reception = receptions.get(indexes[i]);
			selectedReceptions.add(reception);	
		}


        changeStatusDialog.show(selectedReceptions, status);
        if (changeStatusDialog.getResult() == AbstractDialog.OK) {
            Date statusChangeDate = changeStatusDialog.getStatusChangeDate();
            for (Reception reception: selectedReceptions) {
                reception.setStatus(status, statusChangeDate);
                model.updateReception(reception);
            }
            view.tableDataChanged();
        }


	}


    public void selectMainReception(int[] indexes) {
        if (indexes.length == 0) {
            new InfoDialog(view, "Ничего не выбрано", 1000, InfoDialog.RED).showInfo();
            return;
        }
        // Получаем дела, которые выбраны
        List<Reception> receptions = model.getFilteredReceptions();
        StringBuilder selectedReceptionsStringSB = new StringBuilder();
        List<Reception> selectedReceptions = new ArrayList<Reception>();
        for (int i=0; i<indexes.length; i++) {
            Reception reception = receptions.get(indexes[i]);
            selectedReceptions.add(reception);
            selectedReceptionsStringSB.append("<p>").append(reception.getRosreestrCode()).append(";</p>");
        }

        // Формируем список дел, которые нужно исключить из выбора
        List<Reception> receptionsToStrike = new ArrayList<Reception>();
        receptionsToStrike.addAll(selectedReceptions);
        // поддела также убираем, чтобы не было циклов
        for(Reception reception:selectedReceptions)
                receptionsToStrike.addAll(reception.getSubReceptions());

        // Вызываем диалог выбора основного дела, запоминаем
        Reception mainReception = SelectReceptionDialogController.getInstance().showDialog(view,receptionsToStrike);
        if (mainReception==null) return;


        int confirmResult = JOptionPane.showConfirmDialog(view, "<html>Установить для запросов: " +
                        selectedReceptionsStringSB.toString() +
                        " основное дело: " + mainReception.getRosreestrCode() + " ?</html>",
                "Установить основное дело?", JOptionPane.YES_NO_OPTION);

        if (confirmResult == JOptionPane.YES_OPTION) {
            for (Reception reception: selectedReceptions) {
                reception.setParentReception(mainReception);
                model.updateReception(reception);
            }
            view.tableDataChanged();
        }
    }

    public void resetMainReception(int[] indexes) {
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
            selectedReceptionsString += "<p>" + reception.getRosreestrCode() + ";</p>";
        }

        int confirmResult = JOptionPane.showConfirmDialog(view, "<html>Сбросить для дел: " +
                        selectedReceptionsString +
                        " основное дело ?</html>",
                "Сбросить основное дело?", JOptionPane.YES_NO_OPTION);

        if (confirmResult == JOptionPane.YES_OPTION) {
            for (Reception reception: selectedReceptions) {
                reception.setParentReception(null);
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

	public void exportSelectedColumns() {
        ReestrExporterFactory.createReestrExporter(ReestrExporter.Type.SELECTED_COLUMNS).export(model.getFilteredReceptions());
	}

    public void exportForTransmit() {
        ReestrExporterFactory.createReestrExporter(ReestrExporter.Type.FOR_TRANSMIT).export(model.getFilteredReceptions());
    }

    public void exportForArchive() {
        ReestrExporterFactory.createReestrExporter(ReestrExporter.Type.FOR_ARCHIVE).export(model.getFilteredReceptions());
    }

    public void exportForReturn() {
        ReestrExporterFactory.createReestrExporter(ReestrExporter.Type.FOR_RETURN).export(model.getFilteredReceptions());
    }

	// реализует интерфейс для слушателя модели приемов 
	@Override
	public void receptionsModelStateChanged(ServiceState state) {
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