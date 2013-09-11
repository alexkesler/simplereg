package org.kesler.simplereg.gui.reception;

abstract class ReceptionViewState {
	protected ReceptionViewController controller;
	protected ReceptionView view;

	public ReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		this.controller = controller;
		this.view = view;
	}

	abstract void init();

	abstract void back();

	abstract void next();

	abstract void ready();

	void cancel() {
		view.setVisible(false);
	}
}