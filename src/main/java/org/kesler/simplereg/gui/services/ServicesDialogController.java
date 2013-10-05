package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;
import org.kesler.simplereg.dao.EntityState;

/**
* Управляет видом услуг
*/
public class ServicesDialogController {

	private static ServicesDialogController instance;
	private ServicesModel model;
	private ServicesDialog dialog;

	
	public static synchronized ServicesDialogController getInstance() {
		if (instance == null) {
			instance = new ServicesDialogController();
		}

		return instance;
	}

	private ServicesDialogController() {
		this.model = ServicesModel.getInstance();
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

	public void addService() {
		ServiceDialog serviceDialog = new ServiceDialog(dialog);
		serviceDialog.setVisible(true);
	}


	public void editService(Service service) {
		ServiceDialog serviceDialog = new ServiceDialog(dialog, service);
		serviceDialog.setVisible(true);
	}

	public void removeService(Service service) {
		service.setState(EntityState.DELETED);
	}

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
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

		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode subNode = null;
		Service service = null;
		Service subService = null;

		// Получаем список всех узлов дерева
		Enumeration<DefaultMutableTreeNode> nodes = root.breadthFirstEnumeration();

		// Перебираем все элементы дерева 
		while(nodes.hasMoreElements()) {
			node = nodes.nextElement();
			if(node.isRoot()) continue;
			service = (Service)node.getUserObject();
			//System.out.println("Service: " + service.getName());
			Enumeration<DefaultMutableTreeNode> subNodes = root.breadthFirstEnumeration();
			// Для каждого элемента ищем все дочерние элементы
			while(subNodes.hasMoreElements()) {
				subNode = subNodes.nextElement();
				if(subNode.isRoot()) continue; // Если корень - пропускаем
				subService = (Service) subNode.getUserObject();

				if (subService == service) continue;
				if (subService.getParentService() == null) continue;
				if (subService.getParentService().equals(service)) {
					node.add(subNode);
					//System.out.println("adding child:" + subService.getName() +" to parent: " + service.getName());
				}	
			}
		}

		treeModel.reload();


	}

	/**
	* Сохраняет дерево услуг в базу данных
	*/
	public void saveTree() {

		DefaultTreeModel treeModel = (DefaultTreeModel) dialog.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

		Enumeration<DefaultMutableTreeNode> nodes = root.breadthFirstEnumeration();

		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode parentNode = null;
		Service service = null;
		Service parentService = null;

		List<Service> treeServices = new ArrayList<Service>();

		// строим список услуг на основе дерева
		while(nodes.hasMoreElements()) {
			node = nodes.nextElement();
			if(node.isRoot()) continue; // корневой элемент не трогаем
			parentNode = (DefaultMutableTreeNode)node.getParent(); // получаем родительский элемент
			service = (Service)node.getUserObject();
			if (!parentNode.isRoot()) {
				parentService = (Service)parentNode.getUserObject();
				if (!parentService.equals(service.getParentService())) { // если текущий родитель не равен родителю, полученному из дерева
					service.setParentService(parentService); // меняем ролителя
				}
				
			} else { // если родитель для узла - корневой узел, родитель для услуги - пустой
				if (service.getParentService() != null) {
					service.setParentService(null);
				}
				
			}
			treeServices.add(service);
		}

		// сохраняем измененный список

		model.saveServices(treeServices);

	}

}