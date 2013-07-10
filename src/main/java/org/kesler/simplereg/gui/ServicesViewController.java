package org.kesler.simplereg.gui;

import org.kesler.simplereg.logic.ServicesModel;

public class ServicesViewController {

	private ServicesModel model;
	private ServicesView view;

	public ServicesViewController() {
		this.model = ServicesModel.getInstance();
		view = new ServicesView(this);
	}

	public void openView() {
		view.setVisible(true);
	}

}