package org.kesler.simplereg.gui.services;

import javax.swing.JFrame;
import javax.swing.JDialog;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.service.ServicesModel;
import org.kesler.simplereg.logic.service.ServicesModelListener;
import org.kesler.simplereg.logic.ServiceState;


import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;
import org.kesler.simplereg.gui.util.processdialog.ProcessDialogListener;


/**
* Управляет видом услуг
*/
public class ServicesDialogController implements ServicesModelListener, ProcessDialogListener{

	private static ServicesDialogController instance;
	private ServicesModel model;
	private ServicesDialog dialog;

	private ProcessDialog processDialog = null;
	
	public static synchronized ServicesDialogController getInstance() {
		if (instance == null) {
			instance = new ServicesDialogController();
		}

		return instance;
	}

	private ServicesDialogController() {
		this.model = ServicesModel.getInstance();
		model.addServicesModelListener(this);
	}


	public Service openSelectDialog(JFrame parentFrame) {
		Service selectedService = null;

		dialog = new SelectServicesDialog(parentFrame, this);
		reloadTree();
		dialog.setVisible(true);

		if (dialog.getResult() == ServicesDialog.OK) {
			selectedService = dialog.getSelectedService();
		}

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;
		return selectedService;
	}

	public Service openSelectDialog(JDialog parentDialog) {
		Service selectedService = null;

		SelectServicesDialog selectServicesDialog = new SelectServicesDialog(parentDialog, this);
		selectServicesDialog.setLeafSelect(false);
        dialog = selectServicesDialog;
		reloadTree();
        dialog.setVisible(true);

		if (selectServicesDialog.getResult() == ServicesDialog.OK) {
			selectedService = dialog.getSelectedService();
		}

		// Освобождаем ресурсы
		selectServicesDialog.dispose();
		return selectedService;
	}

	public Service openSelectLeafDialog(JDialog parentDialog) {
		Service selectedService = null;

		SelectServicesDialog selectServicesDialog = new SelectServicesDialog(parentDialog, this);
		selectServicesDialog.setLeafSelect(true);
        dialog = selectServicesDialog;
		reloadTree();
		dialog.setVisible(true);

		if (selectServicesDialog.getResult() == ServicesDialog.OK) {
			selectedService = dialog.getSelectedService();
		}

		// Освобождаем ресурсы
		selectServicesDialog.dispose();
		return selectedService;
	}


	public void openEditDialog(JFrame parentFrame) {
		dialog = new EditServicesDialog(parentFrame, this);
		reloadTree();
		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	public void openEditDialog(JDialog parentDialog) {
		dialog = new EditServicesDialog(parentDialog, this);
		reloadTree();
		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}



	public Service addSubService(Service parentService) {

        Service newService = new Service();
        newService.setParentService(parentService); // назначаем ей родительскую услугу
        String code = "";
        if (parentService != null) code = parentService.getCode();
        newService.setCode(code);
        newService.setEnabled(true);

		ServiceDialog serviceDialog = new ServiceDialog(dialog, newService); // вызываем диалог создания услуги
		serviceDialog.setVisible(true);
		if (serviceDialog.getResult() == ServiceDialog.OK) {
//			newService = serviceDialog.getService();    // получаем услугу с наименованием и признаком действующей
			model.addService(newService);               // сохраняем услугу в базу
		}  else {
            newService = null;
        }

		// Освобождаем ресурсы
		serviceDialog.dispose();
		serviceDialog = null;


		return newService;

	}


	public boolean editService(Service service) {
		boolean result = true;
		ServiceDialog serviceDialog = new ServiceDialog(dialog, service);
		serviceDialog.setVisible(true);
		if (serviceDialog.getResult() == ServiceDialog.OK) {
			model.updateService(service);
			result = true;
		} else {
			result = false;	
		}

		// Освобождаем ресурсы
		serviceDialog.dispose();
		serviceDialog = null;

		return result;
	}

	public void removeService(Service service) {

		model.deleteService(service);
		
	}

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
		
		// запускается в отдельном потоке
		processDialog = new ProcessDialog(dialog);
		model.readServicesInSeparateProcess();
		
	}


	public void modelStateChanged(ServiceState state) {
		//if (processDialog == null) return ;
		switch (state) {
			case CONNECTING:
				if (processDialog != null) processDialog.showProcess("Соединяюсь...");			
			break;
			case READING:
				if (processDialog != null) processDialog.showProcess("Читаю список услуг");
			break;	
			case WRITING:
				if (processDialog != null) processDialog.showProcess("Записываю изменения");
			break;	
			case UPDATED:
				dialog.reloadTree(model.getAllServices());
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();	
			break;
			case READY:
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}	
			break;
			case ERROR:				
				if (processDialog != null) {processDialog.hideProcess(); processDialog = null;}
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
			break;			
		}
	}

	@Override
	public void cancelProcess() {
		// model.cancelProcess(thread);
	}

	class ServicesReader implements Runnable {
		public void run() {
			model.readServices();
		}
	}

}