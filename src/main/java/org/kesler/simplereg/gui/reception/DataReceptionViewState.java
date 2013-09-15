package org.kesler.simplereg.gui.reception;

class DataReceptionViewState extends ReceptionViewState {

	DataReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		view.getTabbedPane().setEnabledAt(view.SERVICE_STATE,false);
		view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE,false);
		view.getTabbedPane().setEnabledAt(view.DATA_STATE,true);
		view.getTabbedPane().setEnabledAt(view.PRINT_STATE,false);

		view.getTabbedPane().setSelectedIndex(view.DATA_STATE);

		view.getBackButton().setEnabled(true);
		view.getNextButton().setEnabled(true);
		view.getReadyButton().setEnabled(false);

	}

	@Override
	void back() {
		// Переходим в состояние ввода данных о заявителях
		controller.setState(new ApplicatorsReceptionViewState(controller, view));
	} 

	@Override
	void next() {
		// Переходим в состояние печати запроса
		controller.setState(new PrintReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Ничего не делаем - кнопка невидима
	}

	@Override
	void updatePanelData() {
		
	}


}