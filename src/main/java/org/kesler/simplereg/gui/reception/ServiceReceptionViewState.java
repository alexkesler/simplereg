package org.kesler.simplereg.gui.reception;

class ServiceReceptionViewState extends ReceptionViewState {
	
	ServiceReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		view.getTabbedPane().setSelectedIndex(0);
		view.getBackButton().setVisible(false);
		view.getReadyButton().setVisible(false);
	}

	@Override
	void back() {
		// Ничего не делаем - книпка невидима
	} 

	@Override
	void next() {
		// Переходим в состояние приема заявителя
		controller.setState(new ApplicatorsReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Ничего не делаем - кнопка невидима
	}

}