package org.kesler.simplereg.gui.reception;

import java.util.List;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.main.CurrentOperator;

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

		// Создаем экземпляр приема заявтеля
		reception = new Reception();

		//Получаем текущего оператора
		operator = CurrentOperator.getInstance().getOperator();
		reception.setOperator(operator);
	}

	public void selectService() {
		service = ServicesDialogController.getInstance().openSelectDialog(view);
		String serviceName = "Услуга не определена";
		if (service != null) {
			serviceName = service.getName();
		}
		view.getServiceNameLabel().setText(serviceName);
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

	Reception getReception() {
		return reception;
	}
}