package org.kesler.simplereg.gui.services;

import java.util.List;
import javax.swing.tree.DefaultTreeModel;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.ServicesModel;

public class ServicesViewController {

	private ServicesModel model;
	private ServicesView view;

	public ServicesViewController() {
		this.model = ServicesModel.getInstance();
		view = new ServicesView(this);
		openView();
		readServices();
	}

	public void openView() {
		view.setVisible(true);
	}

	public void reloadTree() {
		List<Service> services = model.getAllServices();

		DefaultTreeModel model = view.getTreeModel();
		TreeNode root = model.getRoot();

		root.removeAllChildren();

		for (Service s : services) {
			root.add(DefaultMutableTreeNode(s));
		}



	}

}