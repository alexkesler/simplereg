package org.kesler.simplereg.gui.reception;

class DataMakeReceptionViewState extends MakeReceptionViewState {

	DataMakeReceptionViewState(MakeReceptionViewController controller, MakeReceptionView view) {
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
		controller.setState(new ApplicatorsMakeReceptionViewState(controller, view));
	} 

	@Override
	void next() {
		// Переходим в состояние печати запроса
		controller.setState(new PrintMakeReceptionViewState(controller, view));
	}

	@Override
	void ready() {
		// Ничего не делаем - кнопка невидима
	}

	@Override
	void updatePanelData() {
		
	}


}