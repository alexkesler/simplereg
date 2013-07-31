package org.kesler.simplereg.gui.operators;

public class OperatorsViewController {
	private OperatorsView view;
	private static OperatorsViewController instance = null;

	private OperatorsViewController() {
		view = new OperatorsView(this);
	}

	public static synchronized OperatorsViewController getInstance() {
		if (instance == null) {
			instance = new OperatorsViewController();
		}

		return instance;
	}

	public void openView() {
		view.setVisible(true);
	}

	
	
}