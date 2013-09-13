package org.kesler.simplereg.gui.reception;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.gui.services.ServicesDialogController;

class ServiceReceptionViewState extends ReceptionViewState {
	
	ServiceReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		view.getTabbedPane().setEnabledAt(view.SERVICE_STATE,true);
		view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE,false);
		view.getTabbedPane().setEnabledAt(view.DATA_STATE,false);
		view.getTabbedPane().setEnabledAt(view.PRINT_STATE,false);

		view.getTabbedPane().setSelectedIndex(view.SERVICE_STATE);

		view.getBackButton().setEnabled(false);
		view.getNextButton().setEnabled(true);
		view.getReadyButton().setEnabled(false);
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