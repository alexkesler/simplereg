package org.kesler.simplereg.gui.main;

import java.util.List;

import org.kesler.simplereg.gui.fias.FIASDialog;
import org.kesler.simplereg.gui.issue.IssueDialogController;
import org.kesler.simplereg.gui.options.OptionsDialog;
import org.kesler.simplereg.gui.pvd.PVDImportDialogController;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.operator.OperatorsModelStateListener;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.pvdimport.Transform;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.transform.TransformException;
import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.operators.OperatorListDialogController;
import org.kesler.simplereg.gui.statistic.StatisticViewController;
import org.kesler.simplereg.gui.reception.MakeReceptionViewController;
import org.kesler.simplereg.gui.reception.ReceptionStatusListDialogController;
import org.kesler.simplereg.gui.applicator.FLListDialogController;
import org.kesler.simplereg.gui.applicator.ULListDialogController;
import org.kesler.simplereg.gui.reestr.ReestrViewController;
import org.kesler.simplereg.gui.realty.RealtyObjectListDialogController;
import org.kesler.simplereg.gui.realty.RealtyTypeListDialogController;
import org.kesler.simplereg.util.OptionsUtil;

import static org.kesler.simplereg.gui.main.MainViewCommand.*;
import javax.swing.*;


/**
* Управляет основным окном приложения
*/
public class MainViewController implements MainViewListener, 
								CurrentOperatorListener, 
								OperatorsModelStateListener, 
								ReceptionsModelStateListener{
	private static MainViewController instance;

	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;
	private RealtyObjectsModel realtyObjectsModel;
	private LoginDialog loginDialog;

	private ProcessDialog processDialog;

	private MainViewController() {
		this.receptionsModel = new ReceptionsModel();
		this.operatorsModel = OperatorsModel.getInstance();
		this.realtyObjectsModel = RealtyObjectsModel.getInstance();

		operatorsModel.addOperatorsModelStateListener(this);
		receptionsModel.addReceptionsModelStateListener(this);
		
		mainView = new MainView(this);
		mainView.addMainViewListener(this);
		
		CurrentOperator.getInstance().addCurrentOperatorListener(this);
	}

	/**
	* Всегда возвращает один и тот же экземпляр контроллера (паттерн Одиночка)
	*/
	public static synchronized MainViewController getInstance() {
		if (instance == null) {
			instance = new MainViewController();
		}
		return instance;
	}

	/**
	* Открывает основное окно приложения
	*/
	public void openMainView() {
		mainView.setVisible(true);
		setMainViewAccess(null);
	}

	/**
	* Обрабатывает команды основного вида, определенные в классе {@link org.kesler.simplereg.gui.main.MainViewCommand}
	*/
	@Override
	public void performMainViewCommand(MainViewCommand command) {
		switch (command) {
			case Login: 
				login();
				break;
			case Logout:
				logout();
				break;
            case About:
                about();
                break;
            case NewReception:
				openMakeReceptionView();
				break;
           case NewReceptionFromPVD:
				openNewReceptionFromPVDView();
				break;
			case UpdateReceptions:
				readReceptions();
				break;
			case OpenReceptionsReestr: 
				openReceptionsReestr();
				break;
			case OpenStatistic: 
				openStatistic();
				break;
			case FLs: 
				openFLs();
				break;
			case ULs: 
				openULs();
				break;
			case Services: 
				openServicesView();
				break;
			case ReceptionStatuses: 
				openReceptionStatuses();
				break;
			case RealtyObjects: 
				openRealtyObjects();
				break;
			case RealtyObjectTypes: 
				openRealtyObjectTypes();
				break;
			case Operators: 
				openOperators();
				break;
			case Options:	
				openOptions();
				break;
            case FIAS:
                openFIASDialog();
                break;
            case Issue:
                openIssueDialog();
                break;
			case Exit:
				System.exit(0);	

		}
	}


	private void setMainViewAccess(Operator operator) {

		// по умолчанию все элементы неактивны
		for (MainViewCommand command: values()) {
			mainView.getActionByCommand(command).setEnabled(false);
		}

		// Элемент Закрыть всегда активен
		mainView.getActionByCommand(Exit).setEnabled(true);
		mainView.getActionByCommand(Options).setEnabled(true);
        mainView.getActionByCommand(About).setEnabled(true);

		
		if (operator != null) { // оператор назначен

			mainView.getActionByCommand(Logout).setEnabled(true);
			mainView.getActionByCommand(NewReception).setEnabled(true);
            mainView.getActionByCommand(Issue).setEnabled(true);
            mainView.getActionByCommand(NewReceptionFromPVD).setEnabled(true);
			mainView.getActionByCommand(UpdateReceptions).setEnabled(true);

			if (operator.isControler()) { // для контролера
				mainView.getActionByCommand(ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(FLs).setEnabled(true);
				mainView.getActionByCommand(ULs).setEnabled(true);
				mainView.getActionByCommand(RealtyObjects).setEnabled(true);
				mainView.getActionByCommand(RealtyObjectTypes).setEnabled(true);
			}

			if (operator.isAdmin()) { // для администратора
				mainView.getActionByCommand(ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(FLs).setEnabled(true);
				mainView.getActionByCommand(ULs).setEnabled(true);
				mainView.getActionByCommand(RealtyObjects).setEnabled(true);
				mainView.getActionByCommand(RealtyObjectTypes).setEnabled(true);
				mainView.getActionByCommand(Services).setEnabled(true);
				mainView.getActionByCommand(Operators).setEnabled(true);
                mainView.getActionByCommand(FIAS).setEnabled(true);

			}

		} else { // если оператор не назначен
			mainView.getActionByCommand(Login).setEnabled(true);
		}

	}


	private void readReceptions() {
		processDialog = new ProcessDialog(mainView);
		receptionsModel.readReceptionsAndApplyFiltersInSeparateThread();

	}

	@Override 
	public void receptionsModelStateChanged(ModelState state) {
		switch (state) {
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь...");
			break;

			case READING:
				if (processDialog != null) processDialog.showProcess("Получаю список приемов");
			break;	

			case WRITING:
				if (processDialog != null) processDialog.showProcess("Сохраняю");
			break;	

			case READY:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
			break;	
			
//			case UPDATED:
//				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
//				mainView.setReceptions(receptionsModel.getAllReceptions());
//			break;

            case FILTERED:
                if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
                mainView.setReceptions(receptionsModel.getFilteredReceptions());
                break;

			case ERROR:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(mainView, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
				
			break;		

		}
	}

	private void login() {			
			
		loginDialog = new LoginDialog(mainView);

		processDialog = new ProcessDialog(loginDialog);
		operatorsModel.readOperatorsInSeparateThread();

		loginDialog.showDialog();

		// делаем проверку на итог - назначаем оператора
		if (loginDialog.getResult() == LoginDialog.OK) {
			Operator operator = loginDialog.getOperator();
			CurrentOperator.getInstance().setOperator(operator);
            mainView.setConnected(true);
			new InfoDialog(mainView, "<html>Добро пожаловать, <p><i>" + 
										operator.getFirstName() + 
										" " + operator.getParentName() + "</i>!</p></html>", 1000, InfoDialog.STAR).showInfo();
		} else {
			CurrentOperator.getInstance().resetOperator();
			HibernateUtil.closeConnection();
            mainView.setConnected(false);
		}
		// Освобождаем ресурсы
		loginDialog.dispose();
		loginDialog = null;

			

	}



	public void operatorsModelStateChanged(ModelState state) {
		switch (state) {
			case CONNECTING:
					
					if (processDialog != null) processDialog.showProcess("Соединяюсь...");
					break;
			
			case READING:
					if (processDialog != null) processDialog.showProcess("Читаю список операторов из базы...");
					break;

			case UPDATED:
					if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
					List<Operator> operators = operatorsModel.getActiveOperators();
					if (loginDialog != null) loginDialog.setOperators(operators);
					break;

			case READY:
					break;


			case ERROR:
					if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
					new InfoDialog(loginDialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
					break;	
			
		}
	}


	private void logout() {
		CurrentOperator.getInstance().resetOperator();
		HibernateUtil.closeConnection();
        mainView.setConnected(false);
	}

    private void about() {
        AboutDialog aboutDialog = new AboutDialog(mainView);
        aboutDialog.showDialog();
    }

	private void openMakeReceptionView() {
		MakeReceptionViewController.getInstance().openView(mainView);
	}

	private void openServicesView() {
		ServicesDialogController.getInstance().openEditDialog(mainView);
	}

	private void openStatistic() {
		StatisticViewController.getInstance().openView();
	}

	private void openOperators() {
		OperatorListDialogController.getInstance().showDialog(mainView);		
	}

	private void openReceptionStatuses() {
		ReceptionStatusListDialogController.getInstance().openDialog(mainView);
	}

	private void openFLs() {
		FLListDialogController.getInstance().openDialog(mainView);
	}

	private void openULs() {
		ULListDialogController.getInstance().openDialog(mainView);
	}

	private void openReceptionsReestr() {
		ReestrViewController.getInstance().openView(mainView);
	}

	private void openOptions() {
		OptionsDialog optionsDialog = new OptionsDialog(mainView);
		optionsDialog.showDialog();
	}

	private void openRealtyObjects() {
		RealtyObjectListDialogController.getInstance().showDialog(mainView);
	}

	private void openRealtyObjectTypes() {
		RealtyTypeListDialogController.getInstance().showDialog(mainView);
	}

    private void openFIASDialog() {
        FIASDialog dialog = new FIASDialog(mainView,true);
        dialog.setVisible(true);
    }

    private void openIssueDialog() {
        IssueDialogController.getInstance().openDialog(mainView);
    }

    public void editReception(Reception reception) {
        MakeReceptionViewController.getInstance().openView(mainView, reception);
    }


    private void openNewReceptionFromPVDView() {
        String pvdServerIp = OptionsUtil.getOption("pvd.serverip");
        if (pvdServerIp==null || pvdServerIp.isEmpty()) {
            JOptionPane.showMessageDialog(mainView,"Адрес сервера не задан, проверьте настройки","Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Получаем номер последнего дела ПК ПВД
        Integer lastPVDNum = receptionsModel.getLastPVDNum();
        Cause cause = null;
        if (lastPVDNum!=null)
            cause = PVDImportDialogController.getInstance().showSelectDialog(mainView, lastPVDNum);
        else // не нашли последнее дело - читаем за текущий день
            cause = PVDImportDialogController.getInstance().showSelectDialog(mainView);
        if (cause==null) return;

        Reception reception = null;
        try {
            reception = Transform.makeReceptionFromCause(cause);
        } catch (TransformException e) {
            JOptionPane.showMessageDialog(mainView,e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }

        MakeReceptionViewController.getInstance().openView(mainView,reception,true);
    }




	/**
	* Обрабатывет событие смены оператора
	*/
	@Override
	public void currentOperatorChanged(Operator operator) {

		if (operator != null) {
			mainView.setCurrentOperatorLabel(operator.getFIO());	
		} else {
			mainView.setCurrentOperatorLabel("");
		}

		setMainViewAccess(operator);
	}

}