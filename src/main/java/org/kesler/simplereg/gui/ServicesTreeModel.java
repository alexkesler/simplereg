package org.kesler.simplereg.gui;

import javax.swing.tree.TreeModel;
import javax.swing.event.TreeModelListener;
import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Service;


class ServicesTreeModel implements TreeModel {
	private List<TreeModelListener> listeners;
	private List<Service> services;
	private List<ServiceTreeNode> serviceNodes;

	public ServicesTreeModel(List<Service> services) {
		this.services = services;
		serviceNodes = new ArrayList<ServiceTreeNode>();
		listeners = new ArrayList<TreeModelListener>();
		calculateNodes();
	}

	private void calculateNodes() {
		for(Service service : services) {
			serviceNodes.add(new ServiceTreeNode(service));
		}

		Service parentService = null;
		for(ServiceTreeNode node : serviceNodes) {
			parentService = node.getService();
			for (ServiceTreeNode nodeTmp : serviceNodes) {
				if (nodeTmp.getParentService().equals(parentService)) {
					node.addChild(nodeTmp);
				}
			}
		}

	}

	public void addTreeModelListener(TreeModelListener tml) {
		listeners.add(tml);
	}

	public Object getChild(Object parent, int index) {
		ServiceTreeNode child = null;
		for (ServiceTreeNode node : serviceNodes) {
			if (node.equals((ServiceTreeNode) parent)) {
				child = node.getChildAt(index);
				break;
			}
		}

		return child;
	}

	public int getChildCount(Object parent) {
		int childCount = 0;
		for (ServiceTreeNode node : serviceNodes) {
			if (((ServiceTreeNode) parent).equals(node)) {
				childCount = node.getChildCount();
				break;
			}
		}

		return childCount;

	}

	public int getIndexOfChild(Object parent, Object child) {
		int index = -1;
		for (ServiceTreeNode node : serviceNodes) {
			index = node.getIndex((ServiceTreeNode)child);
			if (index >= 0) {
				break;
			}
			
		}

		return index;
	}


	public Object getRoot() {
		return null;
	}

	public void removeTreeModelListener(TreeModelListener tml) {
		listeners.remove(tml);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}

}