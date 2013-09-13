package org.kesler.simplereg.gui.reception;

import java.awt.event.ActionEvent;

class PrintReceptionViewState extends ReceptionViewState {

	PrintReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override 
	void init() {
		view.getTabbedPane().setEnabledAt(0,false);
		view.getTabbedPane().setEnabledAt(1,false);
		view.getTabbedPane().setEnabledAt(2,true);

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

	@Override
	public void actionPerformed(ActionEvent ev) {
		
	}

}