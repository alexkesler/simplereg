package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;

/**
* Управляет видом услуг
*/
public class ServicesViewController {

	private static ServicesViewController instance;
	private ServicesModel model;
	private ServicesView view;

	
	public static synchronized ServicesViewController getInstance() {
		if (instance == null) {
			instance = new ServicesViewController();
		}

		return instance;
	}

	private ServicesViewController() {
		this.model = ServicesModel.getInstance();
		view = new ServicesView(this);
		openView();
		reloadTree();
	}

	/**
	* Открывает окно с деревом услуг
	*/
	public void openView() {
		view.setVisible(true);
	}

	public Service openSelectDialog(JFrame frame) {

	}

	public void openEditDialog(JFrame frame) {

	}

	/**
	* Перезагружает дерево услуг из базы данных
	*/
	public void reloadTree() {
		List<Service> services = model.getAllServices();

		DefaultTreeModel model = (DefaultTreeModel)view.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

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

		model.reload();


	}

	/**
	* Сохраняет дерево услуг в базу данных
	*/
	public void saveTree() {

	}

}