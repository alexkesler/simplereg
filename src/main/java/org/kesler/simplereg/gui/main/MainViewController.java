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
	private LoginView loginView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;
	private MainViewState state;

	public MainViewController() {
		this.receptionsModel = ReceptionsModel.getInstance();
		this.operatorsModel = OperatorsModel.getInstance();

		openMainView();
	}

	private void openMainView() {
//		List<Reception> receptions = receptionsModel.getReceptions();
	
		mainView = new MainView(this);
//		mainView.getTableModel().setReceptions(receptions);

		mainView.setVisible(true);
	}

	public void openReceptionView() {
		ReceptionView receptionView = new ReceptionView(this);
		receptionView.setVisible(true);
	}

	public void openServicesView() {
		ServicesViewController servicesViewController = new ServicesViewController();
		servicesViewController.openView();
	}

	public void openLoginView() {
		loginView = new LoginView(this);
		List<Operator> operators = operatorsModel.getActiveOperators();
		loginView.setOperators(operators);
		loginView.setVisible(true);
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
		Operator operator = loginView.getOperator();
		char[] password = operator.getPassword().toCharArray();
		char[] input = loginView.getPassword();
		if (input.length != password.length || !Arrays.equals(input, password)) {
			JOptionPane.showMessageDialog(loginView,
                "Неправильный пароль. Попробуйте еще раз.",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
		} else {
			loginView.setVisible(false);
			CurrentOperator.setOperator(operator);
		}
		Arrays.fill(input,'0');
		Arrays.fill(password, '0');
	}

	public void openStatistic() {
		//StatisticViewController.getInstance().openView();
	}

	public void openOperators() {
		OperatorsViewController.getInstance().openView();
		
	}

	
}