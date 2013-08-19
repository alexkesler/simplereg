package org.kesler.simplereg;

import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.gui.main.MainViewController;
import org.kesler.simplereg.util.PropertiesUtil;

public class SimpleReg {

	public static void main(String[] args) {
		MainViewController.getInstance().openMainView();
		PropertiesUtil.readProperties();
	}

}