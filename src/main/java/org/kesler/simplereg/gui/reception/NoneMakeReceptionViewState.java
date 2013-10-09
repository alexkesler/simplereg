package org.kesler.simplereg.gui.reception;


/// класс, представляющий пустое состояние
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