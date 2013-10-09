package org.kesler.simplereg.gui.reception;

abstract class MakeReceptionViewState {
	protected MakeReceptionViewController controller;
	protected MakeReceptionView view;

	public MakeReceptionViewState(MakeReceptionViewController controller, MakeReceptionView view) {
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

	// Обновляет данные на панели
	abstract void updatePanelData();
}

