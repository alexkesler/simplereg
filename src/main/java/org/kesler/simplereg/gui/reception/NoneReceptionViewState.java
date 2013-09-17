package org.kesler.simplereg.gui.reception;


class NoneReceptionViewState extends ReceptionViewState {

	NoneReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		view.setVisible(false);
	}

	@Override
	void back() {}

	@Override
	void next() {}

	@Override
	void ready() {}

	@Override
	void updatePanelData() {}


}