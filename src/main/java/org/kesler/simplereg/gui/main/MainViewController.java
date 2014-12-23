package org.kesler.simplereg.gui.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.kesler.simplereg.gui.fias.FIASDialog;
import org.kesler.simplereg.gui.issue.IssueDialogController;
import org.kesler.simplereg.gui.options.OptionsDialog;
import org.kesler.simplereg.gui.pvd.PVDImportDialogController;
import org.kesler.simplereg.gui.reception.check.CheckReceptionStatusDialogController;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.operator.OperatorsService;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.operator.OperatorsModelStateListener;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.pvdimport.Transform;
import org.kesler.simplereg.pvdimport.domain.Cause;
import org.kesler.simplereg.pvdimport.transform.TransformException;
import org.kesler.simplereg.util.HibernateUtil;

import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.operators.OperatorListDialogController;
import org.kesler.simplereg.gui.statistic.StatisticViewController;
import org.kesler.simplereg.gui.reception.make.MakeReceptionViewController;
import org.kesler.simplereg.gui.reception.receptionstatus.ReceptionStatusListDialogController;
import org.kesler.simplereg.gui.applicator.FLListDialogController;
import org.kesler.simplereg.gui.applicator.ULListDialogController;
import org.kesler.simplereg.gui.reestr.ReestrViewController;
import org.kesler.simplereg.gui.realty.RealtyObjectListDialogController;
import org.kesler.simplereg.gui.realty.RealtyTypeListDialogController;
import org.kesler.simplereg.util.LoggingUtil;
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
    private static Logger log = Logger.getLogger(MainViewController.class);

	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsService operatorsService;
//	private RealtyObjectsService realtyObjectsService;
	private LoginDialog loginDialog;

	private ProcessDialog processDialog;

	private MainViewController() {
		this.receptionsModel = new ReceptionsModel();
		this.operatorsService = OperatorsService.getInstance();
//		this.realtyObjectsService = RealtyObjectsService.getInstance();

		operatorsService.addOperatorsModelStateListener(this);
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
                log.info("Open Make reception view");
				openMakeReceptionView();
				break;
            case NewReceptionFromPVD:
                log.info("Open New reception from PVD view");
				openNewReceptionFromPVDView();
				break;
            case CheckReceptionStatus:
                log.info("Open CheckReceptionStatusDialog");
				openCheckReceptionStatusDialog();
				break;
			case UpdateReceptions:
                log.info("Update receptions");
				readReceptions();
				break;
			case OpenReceptionsReestr:
                log.info("Open reestr view");
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
                log.info("Open Services");
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
                log.info("Exit.");
				System.exit(0);	

		}
	}


	private void setMainViewAccess(Operator operator) {

        log.info("Setting permissions..");
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
            mainView.getActionByCommand(CheckReceptionStatus).setEnabled(true);
			mainView.getActionByCommand(UpdateReceptions).setEnabled(true);

			if (operator.isControler()) { // для контролера
				mainView.getActionByCommand(ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(FLs).setEnabled(true);
				mainView.getActionByCommand(ULs).setEnabled(true);
				mainView.getActionByCommand(RealtyObjects).setEnabled(true);
				mainView.getActionByCommand(RealtyObjectTypes).setEnabled(true);
				mainView.getActionByCommand(Services).setEnabled(true);
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
        log.info("Permissions set.");

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
		operatorsService.readOperatorsInSeparateThread();

        log.info("Showing login dialog..");
		loginDialog.showDialog();

		// делаем проверку на итог - назначаем оператора
		if (loginDialog.getResult() == LoginDialog.OK) {
			Operator operator = loginDialog.getOperator();
			CurrentOperator.getInstance().setOperator(operator);
            log.info("Login OK for oper: " + operator.getShortFIO());
            String logUser = (operator.getCode().isEmpty()?"00":operator.getCode())+"-"+operator.getSurName();
            LoggingUtil.updateLog4j4User(logUser);
            log.info("Login OK for oper: " + operator.getShortFIO());
            mainView.setConnected(true);
			new InfoDialog(mainView, "<html>Добро пожаловать, <p><i>" + 
										operator.getFirstName() + 
										" " + operator.getParentName() + "</i>!</p></html>", 1000, InfoDialog.STAR).showInfo();
		} else {
			CurrentOperator.getInstance().resetOperator();
            LoggingUtil.reinitLog4j();
            log.info("Login canceled");
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
					List<Operator> operators = operatorsService.getActiveOperators();
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
        log.info("Logout");
		CurrentOperator.getInstance().resetOperator();
        LoggingUtil.reinitLog4j();
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
//        Integer lastPVDPackageNum = receptionsModel.getLastPVDPackageNum();
//        Integer lastPVDPackageNum = null;

//        if (lastPVDPackageNum!=null)
//            cause = PVDImportDialogController.getInstance().showSelectDialog(mainView, lastPVDPackageNum);
//        else // не нашли последнее дело - читаем за текущий день

        Cause cause = PVDImportDialogController.getInstance().showSelectDialog(mainView);
        if (cause==null) return;

        Reception reception = null;
        try {
            reception = Transform.makeReceptionFromCause(cause);
        } catch (TransformException e) {
            JOptionPane.showMessageDialog(mainView,e.getMessage(),"Ошибка",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (reception!=null)
            MakeReceptionViewController.getInstance().openView(mainView,reception,true);
    }

    private void openCheckReceptionStatusDialog() {
        CheckReceptionStatusDialogController.getInstance().showDialog(mainView);
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