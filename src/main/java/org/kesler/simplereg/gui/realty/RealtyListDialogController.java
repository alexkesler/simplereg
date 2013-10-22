package org.kesler.simplereg.gui.realty;

import 

public class RealtyListDialogController {

	private static RealtyListDialogController instance = null;

	private RealtyListDialogController() {}

	public static synchronized RealtyListDialogController getInstance() {
		if (instance == null) {
			instance = new RealtyListDialogController();
		}
		return instance;
	}

}