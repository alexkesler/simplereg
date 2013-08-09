package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.Arrays;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.gui.services.ServicesViewController;
import org.kesler.simplereg.gui.operators.OperatorsViewController;
import org.kesler.simplereg.logic.OperatorsModel;



public class MainViewController {
	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;
	private LoginDialog loginDialog;

	public MainViewController() {
		this.receptionsModel = ReceptionsModel.getInstance();
		this.operatorsModel = OperatorsModel.getInstance();

		openMainView();
	}

	private void openMainView() {
		mainView = new MainView(this);
		mainView.setVisible(true);
	}

	private void setMainViewAccess() {
		Operator currentOperator = CurrentOperator.getOperator();
		if (currentOperator != null) {
			
		}
	}


	public void openReceptionView() {
		ReceptionView receptionView = new ReceptionView(this);
		receptionView.setVisible(true);
	}

	public void openServicesView() {
		ServicesViewController servicesViewController = new ServicesViewController();
		servicesViewController.openView();
	}


	public void addReception(Reception reception) {
		receptionsModel.addReception(reception);
	}

	public void readReceptions() {
		receptionsModel.readReceptionsFromDB();
		List<Reception> receptions = receptionsModel.getReceptions();
		mainView.getTableModel().setReceptions(receptions);
	}

	public void login() {
		//получаем список действующих операторов
		List<Operator> operators = operatorsModel.getActiveOperators();
		// создаем диалог ввода пароля
		loginDialog = new LoginDialog(operators);
		loginDialog.setLocationRelativeTo(mainView);
		loginDialog.setVisible(true);

		// сделать проверку на итог - назначить оператора
		//int value = loginDialog
			
		
	}

	public void openStatistic() {
		//StatisticViewController.getInstance().openView();
	}

	public void openOperators() {
		OperatorsViewController.getInstance().openView();
		
	}

	public void openApplicators() {
		
	}

}