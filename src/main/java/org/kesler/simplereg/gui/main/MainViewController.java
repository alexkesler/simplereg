package org.kesler.simplereg.gui.main;

import java.util.List;


import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.gui.services.ServicesViewController;
import org.kesler.simplereg.logic.OperatorsModel;


public class MainViewController {
	private MainView mainView;
	private ReceptionsModel receptionsModel;
	private OperatorsModel operatorsModel;

	public MainViewController() {
		this.receptionsModel = ReceptionsModel.getInstance();
		this.operatorsModel = OperatorsModel.getInstance();

		openMainView();
	}

	private void openMainView() {
		List<Reception> receptions = receptionsModel.getReceptions();
	
		mainView = new MainView(this);
		mainView.getTableModel().setReceptions(receptions);

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
		LoginView loginView = new LoginView(this);
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

	}

	public void openStatisticView() {
		
	}

	
}