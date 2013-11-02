package org.kesler.simplereg.gui.services;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;
import org.kesler.simplereg.logic.service.ServicesModelListener;
import org.kesler.simplereg.logic.service.ServicesModelState;
import org.kesler.simplereg.dao.EntityState;


import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;

/**
* Управляет видом услуг
*/
public class ServicesDialogController implements ServicesModelListener{

	private static ServicesDialogController instance;
	private ServicesModel model;
	private ServicesDialog dialog;

	private ProcessDialog processDialog;

	
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


	public Service openSelectDialog(JFrame frame) {
		Service selectedService = null;

		dialog = new SelectServicesDialog(frame, this);
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

	public void openEditDialog(JFrame frame) {
		dialog = new EditServicesDialog(frame, this);
		reloadTree();
		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}



	public Service addSubService(Service parentService) {
		Service newService = null;

		ServiceDialog serviceDialog = new ServiceDialog(dialog); // вызываем диалог создания услуги
		serviceDialog.setVisible(true);
		if (serviceDialog.getResult() == ServiceDialog.OK) {
			newService = serviceDialog.getService();    // получаем услугу с наименованием и признаком действующей
			newService.setParentService(parentService); // назначаем ей родительскую услугу
			model.addService(newService);               // сохраняем услугу в базу	
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

		return true;
	}

	public void removeService(Service service) {

		model.deleteService(service);
		
	}

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
		
		// запускается в отдельном потоке
		
		Thread servicesReaderThread = new Thread(new ServicesReader());
		//processDialog = new ProcessDialog(dialog, "Работаю", "Читаю список услуг...");
		servicesReaderThread.start(); 	// Запускаем чтение услуг в отдельном потоке
		
		// processDialog.setVisible(true); // Выводим модальный диалог с кнопкой "Отмена" - ожидаем завершения потока (модальный диалог закроется)

		// if (processDialog.getResult() == ProcessDialog.CANCEL) {
		// 	processDialog.dispose();
		// 	processDialog = null;
		// 	return;
		// }

		// if (processDialog.getResult() == ProcessDialog.ERROR) {
		// 	JOptionPane.showMessageDialog(null, "Ошибка подключения к базе данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
		// 	processDialog.dispose();
		// 	processDialog = null;
		// 	return ;
		// }

		// // Освобождаем ресурсы
		// processDialog.dispose();
		// processDialog = null;
		// // получаем загруженый список


	}

	private void completeReloadingTree() {

		List<Service> services = model.getAllServices();

		dialog.reloadTree(services);

	}

	public void modelStateChanged(ServicesModelState state) {
		//if (processDialog == null) return ;
		switch (state) {
			case CONNECTING:
				ProcessDialog.showProcess(dialog, "Соединяюсь...");			
			break;
			case READING:
				ProcessDialog.showProcess(dialog, "Читаю список услуг");
			break;	
			case UPDATED:
				completeReloadingTree();
				ProcessDialog.hideProcess();

			break;
			case ERROR:				
				ProcessDialog.hideProcess();
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
			break;			
		}
	}

	class ServicesReader implements Runnable {
		public void run() {
			model.readServices();
		}
	}

}