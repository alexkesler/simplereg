package org.kesler.simplereg.gui.reception;

class PrintReceptionViewState extends ReceptionViewState {

	PrintReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override 
	void init() {
		view.getTabbedPane().setSelectedIndex(2);
		view.getBackButton().setVisible(true);
		view.getNextButton().setVisible(false);
		view.getReadyButton().setVisible(true);
	}

	@Override
	void back() {
		controller.setState(new ApplicatorsReceptionViewState(controller, view));
	}

	@Override
	void next() {
		// кнопка неактивна
	}

	@Override
	void ready() {
		// Написать обработчик
	}
}