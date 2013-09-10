package org.kesler.simplereg.gui.reception;

import org.kesler.simplereg.logic.Reception;

public class ReceptionViewController {
	private ReceptionView view;
	private static ReceptionViewController instance;

	private ReceptionViewController() {
		view = new ReceptionView(this);
	}

	public static synchronized ReceptionViewController getInstance() {
		if (instance == null) {
			instance = new ReceptionViewController();
		}
		return instance;
	}

	public void openView() {
		view.setVisible(true);
	}

	public void cancel() {
		view.setVisible(false);
	}

}