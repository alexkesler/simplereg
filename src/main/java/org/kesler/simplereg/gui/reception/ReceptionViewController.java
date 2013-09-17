package org.kesler.simplereg.gui.reception;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

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
import org.kesler.simplereg.gui.applicator.ApplicatorULDialog;

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

		// Переключаем в начальное состояние
		viewState = new ServiceReceptionViewState(this, view);
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

	////// Блок добавления, редактирования, удаления заявителей

	void addApplicatorFL() {
		ApplicatorFLDialog flDialog = new ApplicatorFLDialog(view);
		// Модальный диалог - ожидание закрытия
		flDialog.setVisible(true);
		 
		if (flDialog.getResult() == ApplicatorFLDialog.OK) {
			applicators.add(flDialog.getApplicatorFL());
			view.getApplicatorsPanel().applicatorAdded(applicators.size()-1);
		}
	}

	void addApplicatorUL() {
		ApplicatorULDialog ulDialog = new ApplicatorULDialog(view);

		ulDialog.setVisible(true);
		if (ulDialog.getResult() == ApplicatorULDialog.OK) {
			applicators.add(ulDialog.getApplicatorUL());
			view.getApplicatorsPanel().applicatorAdded(applicators.size()-1);
		}

	}

	void editApplicator(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(view, "Заявитель не выбран.", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;		
		} 
		Applicator currentApplicator = applicators.get(index);
		// Проверяем тип заявителя и вызываем соответствующий диалогс
		if (currentApplicator instanceof ApplicatorFL) {
			ApplicatorFL currentApplicatorFL = (ApplicatorFL) currentApplicator;
			ApplicatorFLDialog flDialog = new ApplicatorFLDialog(view, currentApplicatorFL);
			flDialog.setVisible(true);
			if (flDialog.getResult() == ApplicatorFLDialog.OK) view.getApplicatorsPanel().applicatorUpdated(index);
		} else if (currentApplicator instanceof ApplicatorUL) {
			ApplicatorUL currentApplicatorUL = (ApplicatorUL) currentApplicator;
			ApplicatorULDialog ulDialog = new ApplicatorULDialog(view, currentApplicatorUL);
			ulDialog.setVisible(true);
			if (ulDialog.getResult() == ApplicatorULDialog.OK) view.getApplicatorsPanel().applicatorUpdated(index);
		}

	}

	void removeApplicator(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(view, "Заявитель не выбран.", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;		
		}  

		applicators.remove(index);
		view.getApplicatorsPanel().applicatorRemoved(index);

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