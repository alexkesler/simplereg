package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;
import org.kesler.simplereg.logic.service.ServicesModelListener;
import org.kesler.simplereg.logic.service.ServicesModelState;
import org.kesler.simplereg.dao.EntityState;

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

		dialog = new ServicesDialog(frame, this, ServicesDialog.SELECT);
		reloadTree();
		dialog.setVisible(true);

		selectedService = dialog.getSelectedService();

		return selectedService;
	}

	public void openEditDialog(JFrame frame) {
		dialog = new ServicesDialog(frame, this, ServicesDialog.EDIT);
		reloadTree();
		dialog.setVisible(true);
	}



	public void addSubService(DefaultMutableTreeNode parentNode) {
		ServiceDialog serviceDialog = new ServiceDialog(dialog);
		serviceDialog.setVisible(true);
		if (serviceDialog.getResult() == ServiceDialog.OK) {
			Service newService = serviceDialog.getService();
			Service parentService;
			if (!parentNode.isRoot()) {
				parentService = (Service) parentNode.getUserObject();
			} else {
				parentService = null;
			}
			newService.setParentService(parentService);
			model.addService(newService);
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newService);
			DefaultTreeModel treeModel = (DefaultTreeModel)dialog.getTreeModel();

			treeModel.insertNodeInto(newNode ,parentNode, parentNode.getChildCount());
		}
	}


	public void editService(DefaultMutableTreeNode node) {
		Service service = (Service)node.getUserObject();
		ServiceDialog serviceDialog = new ServiceDialog(dialog, service);
		serviceDialog.setVisible(true);
		if (serviceDialog.getResult() == ServiceDialog.OK) {
			DefaultTreeModel treeModel = (DefaultTreeModel)dialog.getTreeModel();
			model.updateService(service);
			treeModel.nodeChanged(node);
		}
	}

	public void removeService(DefaultMutableTreeNode node) {
		Service service = (Service)node.getUserObject();
		int result = JOptionPane.showConfirmDialog(dialog, "Удалить услугу: " + service + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			DefaultTreeModel treeModel = (DefaultTreeModel)dialog.getTreeModel();
			treeModel.removeNodeFromParent(node);			
			model.deleteService(service);
		}

		
	}

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
		
		// запускается в отдельном потоке
		
		Thread servicesReaderThread = new Thread(new ServicesReader());
		processDialog = new ProcessDialog(dialog, "Работаю", "Читаю список услуг...");
		servicesReaderThread.start(); 	// Запускаем чтение услуг в отдельном потоке
		processDialog.setVisible(true); // Выводим модальный диалог с кнопкой "Отмена" - ожидаем завершения потока (модальный диалог закроется)

		if (processDialog.getResult() == ProcessDialog.CANCEL) {
			return;
		}

		if (processDialog.getResult() == ProcessDialog.ERROR) {
			JOptionPane.showMessageDialog(null, "Ошибка подключения к базе данных", "Ошибка", JOptionPane.ERROR_MESSAGE);
			return ;
		}

		List<Service> services = model.getAllServices();



		// Получаем модель дерева
		DefaultTreeModel treeModel = (DefaultTreeModel) dialog.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

		// Очищаем дерево
		root.removeAllChildren();

		// Добавляем все услуги в первый уровень дерева
		for (Service s : services) {
			root.add(new DefaultMutableTreeNode(s));
		}

		// Получаем список всех узлов дерева
		Enumeration<DefaultMutableTreeNode> nodes = root.breadthFirstEnumeration();

		// копируем все узлы в отдельный список

		List<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
		while (nodes.hasMoreElements()) {
			DefaultMutableTreeNode node = nodes.nextElement();
			nodeList.add(node);			
		}		

		// Перебираем все элементы дерева 
		for (DefaultMutableTreeNode node: nodeList) {
			if(node.isRoot()) continue;
			Service service = (Service)node.getUserObject();
			// System.out.println("ServiceID: " + service.getId());
			Enumeration<DefaultMutableTreeNode> subNodes = root.breadthFirstEnumeration();
			// Для каждого элемента ищем все дочерние элементы
			for (DefaultMutableTreeNode subNode: nodeList) {
				//subNode = subNodes.nextElement();
				if(subNode.isRoot()) continue; // Если корень - пропускаем
				Service subService = (Service) subNode.getUserObject();
				// System.out.println("Subservice? ID: " + subService.getId());

				if (subService == service) continue;
				if (subService.getParentService() == null) continue;
				if (subService.getParentService().equals(service)) {
					node.add(subNode);
					// System.out.println("adding child:" + subService.getName() +" to parent: " + service.getName());
				}	
			}
		}

		treeModel.reload();


	}

	public void modelStateChanged(ServicesModelState state) {
		switch (state) {
			case CONNECTING:
				processDialog.setContent("Соединяюсь...");			
			break;
			case READING:
				processDialog.setContent("Читаю список услуг");
			break;	
			case READY:
				processDialog.setVisible(false);
			break;
			case ERROR:
				processDialog.setResult(ProcessDialog.ERROR);
				processDialog.setVisible(false);
			break;			
		}
	}

	class ServicesReader implements Runnable {
		public void run() {
			model.readServices();
		}
	}

}