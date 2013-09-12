package org.kesler.simplereg.gui.reception;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.gui.services.ServicesViewController;

public class ReceptionViewController {
	private ReceptionView view;
	private static ReceptionViewController instance;
	private ReceptionViewState viewState;
	
	private Operator operator;
	private Service service;
	private List<Applicator> applicators;
	private Reception reception;


	private ReceptionViewController() {
		view = new ReceptionView(this);
		viewState = new NoneReceptionViewState(this, view);
	}

	public static synchronized ReceptionViewController getInstance() {
		if (instance == null) {
			instance = new ReceptionViewController();
		}
		return instance;
	}

	public void openView() {
		view.setVisible(true);
		viewState = new ServiceReceptionViewState(this, view);
		reception = new Reception();
	}

	public void selectService() {
		ServicesViewController.getInstance().openSelectDialog();
	}

	public void back() {
		viewState.back();
	}

	public void next() {
		viewState.next();
	}

	public void ready() {
		viewState.ready();
	}

	public void cancel() {
		reception = null;
		viewState.cancel();
	}

	void setState(ReceptionViewState viewState) {
		this.viewState = viewState;
	}

}