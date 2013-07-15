package org.kesler.simplereg.gui;

import javax.swing.tree.TreeNode;

import org.kesler.simplereg.logic.Service;

public class ServiceTreeNode implements TreeNode {
	private Service service = null;

	private TreeNode parent = null;
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();


	public TreeNode getParent() {
		return parent;
	}

}