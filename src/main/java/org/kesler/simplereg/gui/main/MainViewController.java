package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.Arrays;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.operators.OperatorsViewController;
import org.kesler.simplereg.gui.statistic.StatisticViewController;
import org.kesler.simplereg.gui.reception.ReceptionViewController;
import org.kesler.simplereg.logic.OperatorsModel;


/**
* Управляет основным окном приложения
*/
public class MainViewController implements MainViewListener, CurrentOperatorListener{
	private static MainViewController instance;

	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;
	private LoginDialog loginDialog;

	private MainViewController() {
		this.receptionsModel = ReceptionsModel.getInstance();
		this.operatorsModel = OperatorsModel.getInstance();
		
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
				openReceptionView();
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


	private void openReceptionView() {
		ReceptionViewController.getInstance().openView();
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

	private void openApplicators() {
		
	}

	private void openReceptionsReestr() {

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