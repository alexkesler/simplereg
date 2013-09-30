package org.kesler.simplereg.gui.reception;


class NoneMakeReceptionViewState extends MakeReceptionViewState {

	NoneMakeReceptionViewState(MakeReceptionViewController controller, MakeReceptionView view) {
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