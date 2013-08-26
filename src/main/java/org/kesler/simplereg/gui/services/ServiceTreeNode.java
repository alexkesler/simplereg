package org.kesler.simplereg.gui.services;

import javax.swing.tree.TreeNode;
import javax.swing.tree.MutableTreeNode;
import java.util.Vector;
import java.util.Enumeration;

import org.kesler.simplereg.logic.Service;

class ServiceTreeNode implements MutableTreeNode {
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

	@Override
	public Enumeration children() {
		return children.elements();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public ServiceTreeNode getChildAt(int childIndex) {
		return children.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		ServiceTreeNode serviceTreeNode = (ServiceTreeNode)node;
		return children.indexOf(serviceTreeNode);
	}

	@Override
	public ServiceTreeNode getParent() {
		return parent;
	}

	@Override
	public void setParent(MutableTreeNode parent) {
		this.parent = (ServiceTreeNode) parent;
	}

	@Override
	public boolean isLeaf() {
		boolean isLeaf = true;
		if (children.size() > 0) {
			isLeaf = false;
		}

		return isLeaf;
	}

	@Override
	public void removeFromParent() {

	}

	@Override
	public void setUserObject(Object object) {

	}

	@Override
	public void remove(MutableTreeNode node) {

	}

	@Override
	public void remove(int index) {

	}

	@Override
	public void insert(MutableTreeNode node, int index) {
		
	}

	public void addChild(ServiceTreeNode child) {
		children.add(child);
	}

	@Override
	public String toString() {
		return service.getName();
	}

}