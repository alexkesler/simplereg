package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.Arrays;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModelStateListener;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.ModelState;
import org.kesler.simplereg.logic.operator.OperatorsModelStateListener;
import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyObjectsModel;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.InfoDialog;
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
		this.receptionsModel = ReceptionsModel.getInstance();
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
			case NewReception: 
				openMakeReceptionView();
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
			case Exit:
				System.exit(0);	

		}
	}


	private void setMainViewAccess(Operator operator) {

		// по умолчанию все элементы неактивны
		for (MainViewCommand command: MainViewCommand.values()) {
			mainView.getActionByCommand(command).setEnabled(false);
		}

		// Элемент Закрыть всегда активен
		mainView.getActionByCommand(MainViewCommand.Exit).setEnabled(true);
		mainView.getActionByCommand(MainViewCommand.Options).setEnabled(true);

		
		if (operator != null) { // оператор назначен

			mainView.getActionByCommand(MainViewCommand.Logout).setEnabled(true);
			mainView.getActionByCommand(MainViewCommand.NewReception).setEnabled(true);
			mainView.getActionByCommand(MainViewCommand.UpdateReceptions).setEnabled(true);
			
			if (operator.isControler()) { // для контролера
				mainView.getActionByCommand(MainViewCommand.ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.FLs).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.ULs).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.RealtyObjects).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.RealtyObjectTypes).setEnabled(true);
			}

			if (operator.isAdmin()) { // для администратора
				mainView.getActionByCommand(MainViewCommand.ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.FLs).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.ULs).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.RealtyObjects).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.RealtyObjectTypes).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.Services).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.Operators).setEnabled(true);
				
			}

		} else { // если оператор не назначен
			mainView.getActionByCommand(MainViewCommand.Login).setEnabled(true);			
		}

	}


	private void readReceptions() {
		processDialog = new ProcessDialog(mainView);
		receptionsModel.readReceptionsInSeparateThread();

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
			
			case UPDATED:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				List<Reception> receptions = receptionsModel.getAllReceptions();
				mainView.setReceptions(receptions);
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
			new InfoDialog(mainView, "<html>Добро пожаловать, <p><i>" + 
										operator.getFirstName() + 
										" " + operator.getParentName() + "</i>!</p></html>", 1000, InfoDialog.STAR).showInfo();
		} else {
			CurrentOperator.getInstance().resetOperator();
			HibernateUtil.closeConnection();
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
					if (processDialog != null) processDialog.hideProcess();
					List<Operator> operators = operatorsModel.getActiveOperators();
					if (loginDialog != null) loginDialog.setOperators(operators);
					break;

			case READY:
					break;


			case ERROR:
					if (processDialog != null) processDialog.hideProcess();
					new InfoDialog(loginDialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
					break;	
			
		}
	}


	private void logout() {
		CurrentOperator.getInstance().resetOperator();
		HibernateUtil.closeConnection();
	}

	private void openMakeReceptionView() {
		MakeReceptionViewController.getInstance().openView();
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