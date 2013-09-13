package org.kesler.simplereg.gui.reception;

import java.awt.event.ActionEvent;

class NoneReceptionViewState extends ReceptionViewState {

	NoneReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
	}

	@Override
	void init() {}

	@Override
	void back() {}

	@Override
	void next() {}

	@Override
	void ready() {}

	@Override
	public void actionPerformed(ActionEvent ev) {
		
	}

}