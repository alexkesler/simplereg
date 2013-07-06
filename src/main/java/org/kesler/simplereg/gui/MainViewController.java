package org.kesler.simplereg.gui;

import java.util.List;


import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.logic.Reception;


public class MainViewController {
	private MainView mainView;
	private ReceptionsModel receptionsModel;

	public MainViewController(ReceptionsModel receptionsModel) {
		this.receptionsModel = receptionsModel;

		openMainView();

	}

	private void openMainView() {
//		receptionsModel.readReceptionsFromDB();
		List<Reception> receptions = receptionsModel.getReceptions();

		
		mainView = new MainView();
		mainView.getTableModel().setReceptions(receptions);

		mainView.setVisible(true);

	}

	
}