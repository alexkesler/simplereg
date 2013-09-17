package org.kesler.simplereg.gui.reception;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractListModel;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.ReceptionsModel;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.gui.applicator.ApplicatorFLDialog;

public class ReceptionViewController {

	private ReceptionView view;
	private static ReceptionViewController instance;
	private ReceptionViewState viewState;
	
	private Operator operator;
	private Service service;
	private List<Applicator> applicators;
	private Reception reception;


	private ReceptionViewController() {
		applicators = new ArrayList<Applicator>();
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

		//Фиксируем время приема
		reception.setOpenDate(new Date());

		//Получаем текущего оператора
		operator = CurrentOperator.getInstance().getOperator();
		reception.setOperator(operator);

		// Сбрасываем услугу
		service = null;

		// Создаем пустой список заявителей
		applicators = new ArrayList<Applicator>();
	}

	void back() {
		viewState.back();
	}

	void next() {
		viewState.next();
	}

	void ready() {
		viewState.ready();
	}

	void cancel() {
		reception = null;
		viewState.cancel();
	}

	void setState(ReceptionViewState viewState) {
		this.viewState = viewState;
	}

	Reception getReception() {
		return reception;
	}

	List<Applicator> getApplicators() {
		return applicators;
	}

	Service getService() {
		return service;
	}

	void selectService() {
		service = ServicesDialogController.getInstance().openSelectDialog(view);		
		viewState.updatePanelData();

	}

	void addApplicatorFL() {
		ApplicatorFLDialog dialog = new ApplicatorFLDialog(view);
		// Модальный диалог - ожидание закрытия
		dialog.setVisible(true);
		ApplicatorFL applicatorFL = dialog.getApplicatorFL();
		if (applicatorFL != null) {
			applicators.add(applicatorFL);
			view.getApplicatorsPanel().applicatorAdded(applicators.size()-1);
		}
	}

	void addApplicatorUL() {

	}

	void storeService() {
		reception.setService(service);
	}

	void storeApplicators() {
		reception.setApplicators(applicators);
	}

	void saveReception() {

		ReceptionsModel.getInstance().addReception(reception);
	}

}