package org.kesler.simplereg.gui.services;

import java.util.List;
import java.util.Enumeration;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;

public class ServicesViewController {

	private ServicesModel model;
	private ServicesView view;

	public ServicesViewController() {
		this.model = ServicesModel.getInstance();
		view = new ServicesView(this);
		openView();
		reloadTree();
	}

	public void openView() {
		view.setVisible(true);
	}

	public void reloadTree() {
		List<Service> services = model.getAllServices();

		System.out.println("Num of services: " + services.size());

		DefaultTreeModel model = (DefaultTreeModel)view.getTreeModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		root.removeAllChildren();

		for (Service s : services) {
			model.insertNodeInto(new DefaultMutableTreeNode(s), root, root.getChildCount());
			//System.out.println("Incerting: " + s.getName() + " childCnt: " + root.getChildCount());
		}

		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode subNode = null;
		Service service = null;
		Service subService = null;

		Enumeration<DefaultMutableTreeNode> nodes = root.breadthFirstEnumeration();

		while(nodes.hasMoreElements()) {
			node = nodes.nextElement();
			if(node.isRoot()) continue;
			service = (Service)node.getUserObject();
			//System.out.println("Service: " + service.getName());
			Enumeration<DefaultMutableTreeNode> subNodes = root.breadthFirstEnumeration();
			while(subNodes.hasMoreElements()) {
				subNode = subNodes.nextElement();
				if(subNode.isRoot()) continue;
				subService = (Service) subNode.getUserObject();
				//System.out.println("   SubService: " + subService.getName());
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

}