package org.kesler.simplereg.gui;

import javax.swing.tree.TreeNode;
import java.util.Vector;
import java.util.Enumeration;

import org.kesler.simplereg.logic.Service;

class ServiceTreeNode implements TreeNode {
	private Service service = null;

	private ServiceTreeNode parent = null;
	private Vector<ServiceTreeNode> children = new Vector<ServiceTreeNode>();

	public ServiceTreeNode() {

	}

	public ServiceTreeNode(Service service) {
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public Service getParentService() {
		return service.getParentService();
	}

	public Enumeration children() {
		return children.elements();
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public ServiceTreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	public int getChildCount() {
		return children.size();
	}

	public int getIndex(TreeNode node) {
		ServiceTreeNode serviceTreeNode = (ServiceTreeNode)node;
		return children.indexOf(serviceTreeNode);
	}

	public ServiceTreeNode getParent() {
		return parent;
	}

	public void setParent(ServiceTreeNode parent) {
		this.parent = parent;
	}

	public boolean isLeaf() {
		boolean isLeaf = true;
		if (children.size() > 0) {
			isLeaf = false;
		}

		return isLeaf;
	}


	public void addChild(ServiceTreeNode child) {
		children.add(child);
	}

	@Override
	public String toString() {
		return service.getName();
	}

}