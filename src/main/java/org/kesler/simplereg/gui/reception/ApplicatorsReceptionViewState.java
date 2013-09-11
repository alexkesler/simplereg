package org.kesler.simplereg.gui.reception;

class ApplicatorsReceptionViewState extends ReceptionViewState {

	ApplicatorsReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	} 

	@Override 
	void init() {
		view.getTabbedPane().setSelectedIndex(1);
		view.getBackButton().setVisible(true);
		view.getNextButton().setVisible(true);
		view.getReadyButton().setVisible(false);
	}

	@Override
	void back() {
		// Переходим назад к выбору услуги
		controller.setState(new ServiceReceptionViewState(controller, view));
	}

	@Override
	void next() {
		// переходим к печати
		controller.setState(new PrintReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Кнопка неактивна
	}

}