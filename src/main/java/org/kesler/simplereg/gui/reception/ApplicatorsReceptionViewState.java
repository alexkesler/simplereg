package org.kesler.simplereg.gui.reception;

import java.awt.event.ActionEvent;

class ApplicatorsReceptionViewState extends ReceptionViewState {

	ApplicatorsReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	} 

	@Override 
	void init() {
		view.getTabbedPane().setEnabledAt(0,false);
		view.getTabbedPane().setEnabledAt(1,true);
		view.getTabbedPane().setEnabledAt(2,false);

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

	@Override
	public void actionPerformed(ActionEvent ev) {
		
	}

}