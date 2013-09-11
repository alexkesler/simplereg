package org.kesler.simplereg.gui.reception;

import org.kesler.simplereg.logic.Reception;

public class ReceptionViewController {
	private ReceptionView view;
	private static ReceptionViewController instance;
	private ReceptionViewState viewState;

	private ReceptionViewController() {
		view = new ReceptionView(this);
		viewState = new ServiceReceptionViewState(this, view);
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

	public void back() {
		viewState.back();
	}

	public void next() {
		viewState.next();
	}

	public void ready() {
		viewState.ready();
	}

	public void cancel() {
		viewState.cancel();
	}

	void setState(ReceptionViewState viewState) {
		this.viewState = viewState;
	}

}