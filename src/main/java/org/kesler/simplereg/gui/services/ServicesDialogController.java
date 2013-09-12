package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;

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
//		view = new ServicesDialog(this);
//		openView();
//		reloadTree();
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

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
		List<Service> services = model.getAllServices();

		DefaultTreeModel treeModel = (DefaultTreeModel) dialog.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();

		// Очищаем дерево
		root.removeAllChildren();

		// Добавляем все услуги
		for (Service s : services) {
			root.add(new DefaultMutableTreeNode(s));
		}

		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode subNode = null;
		Service service = null;
		Service subService = null;

		// Перебираем все элементы дерева 
		Enumeration<DefaultMutableTreeNode> nodes = root.breadthFirstEnumeration();

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

	}

}