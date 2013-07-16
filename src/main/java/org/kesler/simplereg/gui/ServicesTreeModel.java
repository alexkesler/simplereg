package org.kesler.simplereg.gui;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Service;


class ServicesTreeModel implements TreeModel {
	private List<TreeModelListener> listeners;
	private List<Service> services;
	private List<ServiceTreeNode> serviceNodes;
	private ServiceTreeNode rootNode = null;

	public ServicesTreeModel() {
		services = new ArrayList<Service>();
		serviceNodes = new ArrayList<ServiceTreeNode>();
		listeners = new ArrayList<TreeModelListener>();		
	}

	public ServicesTreeModel(List<Service> services) {
		this.services = services;
		serviceNodes = new ArrayList<ServiceTreeNode>();
		listeners = new ArrayList<TreeModelListener>();
		calculateNodes();
		fireTreeStructureChanged();
	}

	public void setServiceList(List<Service> services) {
		this.services = services;
		calculateNodes();
		fireTreeStructureChanged();
	}

	private void calculateNodes() {

		for(Service service : services) {
			serviceNodes.add(new ServiceTreeNode(service));
		}

		Service currentService = null;
		for(ServiceTreeNode node : serviceNodes) {
			currentService = node.getService();
			for (ServiceTreeNode nodeTmp : serviceNodes) {
				if (currentService.equals(nodeTmp.getParentService())) {
					node.addChild(nodeTmp);
					nodeTmp.setParent(node);
				}
			}
		}


		rootNode = new ServiceTreeNode(new Service("Корневая услуга"));

		for(ServiceTreeNode node : serviceNodes) {
			if (node.getParent()==null) {
				node.setParent(rootNode);
				rootNode.addChild(node);
			}
		}
		serviceNodes.add(rootNode);

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
		return rootNode;
	}

	public void addTreeModelListener(TreeModelListener tml) {
		listeners.add(tml);
	}

	public void removeTreeModelListener(TreeModelListener tml) {
		listeners.remove(tml);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		
	}

	public boolean isLeaf(Object node) {
		ServiceTreeNode serviceNode = (ServiceTreeNode)node;
		if (serviceNode.getChildCount() == 0) {
			return true;
		} else {
			return false;
		}

	}

	private void fireTreeStructureChanged() {
		TreePath treePath = new TreePath(rootNode);
		TreeModelEvent ev = new TreeModelEvent(this, treePath);
		for (TreeModelListener listener : listeners) {
			listener.treeStructureChanged(ev);
		}
	}

}