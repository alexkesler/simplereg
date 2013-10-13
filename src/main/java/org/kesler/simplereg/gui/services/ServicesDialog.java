package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import org.kesler.simplereg.util.ResourcesUtil;

import org.kesler.simplereg.logic.Service;

public abstract class ServicesDialog extends JDialog{

	public static final int NONE = -1;
	public static final int OK = 0;
	public static final int CANCEL = 1;

	protected int result = NONE;

	protected JFrame parentFrame;

	protected ServicesDialogController controller;
	protected JTree servicesTree;
	protected DefaultTreeModel servicesTreeModel;

	protected DefaultMutableTreeNode selectedNode = null;
	protected Service selectedService = null;


	public ServicesDialog(JFrame parentFrame, ServicesDialogController controller) {
		super(parentFrame, true);
		this.parentFrame = parentFrame;
		this.controller = controller;	

		createGUI();
		servicesTreeModel = (DefaultTreeModel) servicesTree.getModel();

	}

	protected abstract void createGUI();

	public int getResult() {
		return result;
	}

	// /**
	// * Возвращает модель дерева услуг, привязанную к виду 
	// */
	// public TreeModel getTreeModel() {
	// 	return servicesTree.getModel();
	// }

	/**
	* Возвращает выбранную услугу
	*/
	public Service getSelectedService() {
		return selectedService;
	}

	protected void reloadTree(List<Service> services) {


		// Получаем модель дерева
		// DefaultTreeModel treeModel = (DefaultTreeModel) servicesTree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) servicesTreeModel.getRoot();

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

		servicesTreeModel.reload();

	}

}