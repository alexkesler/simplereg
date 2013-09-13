package org.kesler.simplereg.gui.reception;

import java.awt.event.ActionEvent;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.gui.services.ServicesDialogController;

class ServiceReceptionViewState extends ReceptionViewState {
	
	ServiceReceptionViewState(ReceptionViewController controller, ReceptionView view) {
		super(controller, view);
		init();
	}

	@Override
	void init() {
		view.getTabbedPane().setEnabledAt(0,true);
		view.getTabbedPane().setEnabledAt(1,false);
		view.getTabbedPane().setEnabledAt(2,false);

		view.getTabbedPane().setSelectedIndex(0);

		view.getBackButton().setVisible(false);
		view.getReadyButton().setVisible(false);
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

	@Override
	public void actionPerformed(ActionEvent ev) {
		if ("Выбрать".equals(ev.getActionCommand())) selectService();
		
	}

	private void selectService() {
		Service service = ServicesDialogController.getInstance().openSelectDialog(view);
		controller.setService(service);

		String serviceName = "Услуга не определена";
		if (service != null) {
			serviceName = service.getName();
		}
		
		view.getServicePanel().getServiceNameLabel().setText(serviceName);

	}

}