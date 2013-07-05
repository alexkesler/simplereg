package org.kesler.simplereg;

import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.gui.MainViewController;

public class SimpleReg {

	public static void main(String[] args) {
		ReceptionsModel receptionsModel = new ReceptionsModel();
		receptionsModel.readReceptionsFromDB();
		MainViewController mainViewController = new MainViewController(receptionsModel);

	}

}