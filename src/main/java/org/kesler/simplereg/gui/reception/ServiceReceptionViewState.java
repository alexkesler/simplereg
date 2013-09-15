package org.kesler.simplereg.gui.reception;

import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.gui.services.ServicesDialogController;

class ServiceReceptionViewState extends ReceptionViewState {
	
	ServiceReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {

		updateTabbedPaneState();
		updateCommonButtons();
		updatePanelData();
	}

	@Override
	void back() {
		// Ничего не делаем - книпка невидима
	} 

	@Override
	void next() {
		// Переходим в состояние приема заявителя, если все в порядке
		if (controller.getService()!=null) {
			controller.setState(new ApplicatorsReceptionViewState(controller, view));
		} else {
			JOptionPane.showMessageDialog(view,
    									"Услуга не выбрана. Пожалуйста, выберите услугу.",
    									"Ошибка",
    									JOptionPane.ERROR_MESSAGE);
			
		}
		
	}

	@Override
	void ready() {
		// Ничего не делаем - кнопка невидима
	}

	private void updateTabbedPaneState() {
		view.getTabbedPane().setEnabledAt(view.SERVICE_STATE,true);
		view.getTabbedPane().setEnabledAt(view.APPLICATORS_STATE,false);
		view.getTabbedPane().setEnabledAt(view.DATA_STATE,false);
		view.getTabbedPane().setEnabledAt(view.PRINT_STATE,false);

		view.getTabbedPane().setSelectedIndex(view.SERVICE_STATE);

	}

	private void updateCommonButtons() {
		view.getBackButton().setEnabled(false);
		view.getNextButton().setEnabled(true);
		view.getReadyButton().setEnabled(false);

	}

	@Override
	void updatePanelData() {
		String serviceName = "Не определена";
		Service service = controller.getService();
		if (service != null) serviceName = service.getName();

		view.getServicePanel().setServiceName(serviceName);
	}



}