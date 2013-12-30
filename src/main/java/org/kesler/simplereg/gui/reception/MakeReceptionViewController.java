package org.kesler.simplereg.gui.reception;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;
import org.kesler.simplereg.gui.services.ServicesDialogController;
import org.kesler.simplereg.gui.main.CurrentOperator;
import org.kesler.simplereg.gui.applicator.ApplicatorFLDialog;
import org.kesler.simplereg.gui.applicator.ApplicatorULDialog;
import org.kesler.simplereg.gui.realty.RealtyObjectListDialogController;
import org.kesler.simplereg.export.ReceptionPrinter;
import org.kesler.simplereg.export.RosReestrReceptionPrinter;


import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.util.OptionsUtil;

public class MakeReceptionViewController {

	private static MakeReceptionViewController instance;
	private MakeReceptionView view;
	private MakeReceptionViewState viewState;

	JFrame parentFrame;
	JDialog parentDialog;
	boolean isNew = true;
	
	private Reception reception;


	private MakeReceptionViewController() {
		initReception();
		// view = new MakeReceptionView(this);
		viewState = new NoneMakeReceptionViewState(this, view);
	}

	public static synchronized MakeReceptionViewController getInstance() {
		if (instance == null) {
			instance = new MakeReceptionViewController();
		}
		return instance;
	}

	public void openView(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		initReception();
		isNew = true;
		
		view = new MakeReceptionView(this, parentFrame);
		// Переключаем в начальное состояние
		viewState = new ServiceMakeReceptionViewState(this, view);
		view.showView();
		view = null;

	}

	public void openView(JDialog parentDialog) {
		this.parentDialog = parentDialog;
		initReception();
		isNew = true;

		view = new MakeReceptionView(this, parentDialog);
		// Переключаем в начальное состояние
		viewState = new ServiceMakeReceptionViewState(this, view);
		view.showView();
		view = null;

	}

	public void openView(JFrame parentFrame, Reception reception) {
		this.parentFrame = parentFrame;
		this.reception = reception;
		isNew = false;

		// Создаем новый вид
		view = new MakeReceptionView(this, parentFrame);
		// Переключаем в начальное состояние
		viewState = new ServiceMakeReceptionViewState(this, view);

		view.showView();
		view = null;
	}

	public void openView(JDialog parentDialog, Reception reception) {
		this.parentDialog = parentDialog;
		this.reception = reception;
		isNew = false;

		// Создаем новый вид
		view = new MakeReceptionView(this, parentFrame);
		// Переключаем в начальное состояние
		viewState = new ServiceMakeReceptionViewState(this, view);		

		view.showView();
		view = null;
	}

	private void initReception() {
		// Создаем экземпляр приема заявтеля
		reception = new Reception();

		//Фиксируем время приема
		reception.setOpenDate(new Date());

		//Получаем текущего оператора
		Operator operator = CurrentOperator.getInstance().getOperator();
		reception.setOperator(operator);

		// Присваиваем номер филиала
		reception.setFilialCode(OptionsUtil.getOption("reg.filial"));

		// Генерируем код дела
		reception.generateReceptionCode();

		// Устанавливаем начальные значения
		reception.setByRecord(false);
		reception.setResultInMFC(false);

		// Создаем пустой список заявителей
		reception.setApplicators(new ArrayList<Applicator>());




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

	void setState(MakeReceptionViewState viewState) {
		this.viewState = viewState;
	}

	Reception getReception() {
		return reception;
	}

	List<Applicator> getApplicators() {
		return reception.getApplicators();
	}

	Service getService() {
		return reception.getService();
	}

	RealtyObject getRealtyObject() {
		return reception.getRealtyObject();
	}


	// Методы для установки свойств Reception из ServicesPanel
	void setReceptionCode(String receptionCode) {
		reception.setReceptionCode(receptionCode);
	}

	void selectService() {
		Service service = ServicesDialogController.getInstance().openSelectDialog(view);	
		reception.setService(service);	
		viewState.updatePanelData();

	}

	void setReceptionByRecord(boolean byRecord) {
		reception.setByRecord(byRecord);
	}

	////// Блок добавления, редактирования, удаления заявителей

	void addApplicatorFL() {
		ApplicatorFLDialog flDialog = new ApplicatorFLDialog(view);
		// Модальный диалог - ожидание закрытия
		flDialog.setVisible(true);
		 
		if (flDialog.getResult() == ApplicatorFLDialog.OK) {
			List<Applicator> applicators = reception.getApplicators();
			Applicator applicator = flDialog.getApplicatorFL();
			applicator.setReception(reception);
			applicators.add(applicator);
			view.getApplicatorsPanel().applicatorAdded(applicators.size()-1);
		}

	}

	void addApplicatorUL() {
		ApplicatorULDialog ulDialog = new ApplicatorULDialog(view);

		ulDialog.setVisible(true);
		if (ulDialog.getResult() == ApplicatorULDialog.OK) {
			List<Applicator> applicators = reception.getApplicators();
			Applicator applicator = ulDialog.getApplicatorUL();
			applicator.setReception(reception);
			applicators.add(applicator);
			view.getApplicatorsPanel().applicatorAdded(applicators.size()-1);
		}

	}

	void editApplicator(int index) {
		if (index == -1) {
			JOptionPane.showMessageDialog(view, "Заявитель не выбран.", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return;		
		} 
		List<Applicator> applicators = reception.getApplicators();
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
	
		List<Applicator> applicators = reception.getApplicators();

		applicators.remove(index);
		view.getApplicatorsPanel().applicatorRemoved(index);

	} 

	/// блок ввода дополнительных данных по приему

	void selectRealtyObject() {

		RealtyObject realtyObject = RealtyObjectListDialogController.getInstance().showSelectDialog(view);
		reception.setRealtyObject(realtyObject);
		viewState.updatePanelData();
	}

	void setToIssueDate(Date toIssueDate) {
		reception.setToIssueDate(toIssueDate);
	}

	void setRosreestrCode(String rosreestrCode) {
		reception.setRosreestrCode(rosreestrCode);
	}

	void setResultInMFC(boolean resultInMFC) {
		reception.setResultInMFC(resultInMFC);
	}

	void printRequest() {
		ReceptionPrinter printer = new RosReestrReceptionPrinter(reception);
		printer.printReception();
	}

	boolean saveReception() {

        ReceptionsModel model = new ReceptionsModel();

		if (isNew) {
			model.addReception(reception);
		} else {
			model.updateReception(reception);
		}
		


		if (parentFrame!=null) {
			new InfoDialog(parentFrame, "Сохранено", 500, InfoDialog.GREEN).showInfo();
		} else if (parentDialog!=null) {
			new InfoDialog(parentDialog, "Сохранено", 500, InfoDialog.GREEN).showInfo();
        }
		
		return true;
	}

}