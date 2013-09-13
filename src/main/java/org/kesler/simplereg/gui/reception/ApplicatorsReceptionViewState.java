package org.kesler.simplereg.gui.reception;

class ApplicatorsReceptionViewState extends ReceptionViewState {

	ApplicatorsReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	} 

	@Override 
	void init() {
		view.getTabbedPane().setEnabledAt(view.SERVICE_STATE,false);
		view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE,true);
		view.getTabbedPane().setEnabledAt(view.DATA_STATE,false);
		view.getTabbedPane().setEnabledAt(view.PRINT_STATE,false);

		view.getTabbedPane().setSelectedIndex(view.APPLICATORS_STATE);

		view.getBackButton().setEnabled(true);
		view.getNextButton().setEnabled(true);
		view.getReadyButton().setEnabled(false);

	}

	@Override
	void back() {
		// Переходим назад к выбору услуги
		controller.setState(new ServiceReceptionViewState(controller, view));
	}

	@Override
	void next() {
		// переходим к печати
		controller.setState(new DataReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Кнопка неактивна
	}


}