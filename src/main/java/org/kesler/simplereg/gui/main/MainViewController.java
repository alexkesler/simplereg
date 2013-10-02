package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.Arrays;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.operator.OperatorsModelListener;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.operators.OperatorsViewController;
import org.kesler.simplereg.gui.statistic.StatisticViewController;
import org.kesler.simplereg.gui.reception.MakeReceptionViewController;
import org.kesler.simplereg.gui.reception.ReceptionStatusListDialogController;
import org.kesler.simplereg.gui.reestr.ReestrViewController;
import org.kesler.simplereg.logic.operator.OperatorsModel;


/**
* Управляет основным окном приложения
*/
public class MainViewController implements MainViewListener, CurrentOperatorListener, OperatorsModelListener{
	private static MainViewController instance;

	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;
	private LoginDialog loginDialog;

	private ProcessDialog processDialog;

	private MainViewController() {
		this.receptionsModel = ReceptionsModel.getInstance();
		this.operatorsModel = OperatorsModel.getInstance();
		operatorsModel.addOperatorsModelListener(this);
		
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
			case OpenApplicators: 
				openApplicators();
				break;
			case Services: 
				openServicesView();
				break;
			case ReceptionStatuses: 
				openReceptionStatuses();
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
			
			if (operator.getIsControler()) { // для контролера
				mainView.getActionByCommand(MainViewCommand.ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenApplicators).setEnabled(true);
			}

			if (operator.getIsAdmin()) { // для администратора
				mainView.getActionByCommand(MainViewCommand.ReceptionStatuses).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenReceptionsReestr).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenStatistic).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.OpenApplicators).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.Services).setEnabled(true);
				mainView.getActionByCommand(MainViewCommand.Operators).setEnabled(true);
				
			}

		} else { // если оператор не назначен
			mainView.getActionByCommand(MainViewCommand.Login).setEnabled(true);			
		}

	}


	private void openMakeReceptionView() {
		MakeReceptionViewController.getInstance().openView();
	}

	private void openServicesView() {
		ServicesDialogController.getInstance().openEditDialog(mainView);
	}

	private void addReception(Reception reception) {
		receptionsModel.addReception(reception);
	}

	private void readReceptions() {
		receptionsModel.readReceptionsFromDB();
		List<Reception> receptions = receptionsModel.getReceptions();
		mainView.getTableModel().setReceptions(receptions);
	}

	private void login() {
		
		processDialog = new ProcessDialog(mainView, "Подключение", "Получаем список операторов...");

		operatorsModel.readOperators();
		processDialog.setVisible(true);

		if (processDialog.getResult() != ProcessDialog.CANCEL) {
			//получаем список действующих операторов
			List<Operator> operators = operatorsModel.getActiveOperators();
			// создаем диалог ввода пароля
			
			loginDialog = new LoginDialog(mainView, operators);
			loginDialog.showDialog();

			// делаем проверку на итог - назначаем оператора
			if (loginDialog.isLoginOk()) {
				CurrentOperator.getInstance().setOperator(loginDialog.getOperator());
			} else {
				CurrentOperator.getInstance().resetOperator();
			}
			
		} else {
			operatorsModel.stopReadOperators();
		}

	}

	public void operatorsChanged(int status) {
		switch (status) {
			case OperatorsModel.STATUS_UPDATING:
				break;
			case OperatorsModel.STATUS_UPDATED:
				processDialog.setVisible(false);
				break;
			case OperatorsModel.STATUS_ERROR:
				break;	
			
		}
	}



	private void logout() {
		CurrentOperator.getInstance().resetOperator();
	}

	private void openStatistic() {
		StatisticViewController.getInstance().openView();
	}

	private void openOperators() {
		OperatorsViewController.getInstance().openView();		
	}

	private void openReceptionStatuses() {
		ReceptionStatusListDialogController.getInstance().openDialog(mainView);
	}

	private void openApplicators() {
		
	}

	private void openReceptionsReestr() {
		ReestrViewController.getInstance().openView();
	}

	private void openOptions() {
		OptionsDialog optionsDialog = new OptionsDialog(mainView);
		optionsDialog.showDialog();
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