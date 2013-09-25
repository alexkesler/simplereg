package org.kesler.simplereg.gui.reestr;

import java.util.List;

import org.kesler.simplereg.logic.Reception;
import org.kesler.simplereg.logic.ReceptionsModel;

public class ReestrViewController {

	private ReceptionsModel model;

	private static ReestrViewController instance = null;

	private ReestrView view;

	public static synchronized ReestrViewController getInstance() {
		if (instance == null) {
			instance = new ReestrViewController();
		}
		return instance;
	}

	private ReestrViewController() {
		model = ReceptionsModel.getInstance();
		view = new ReestrView(this);
	}

	public void openView() {
		view.setVisible(true);
	}

	List<Reception> getReceptionsList() {
		return model.getReceptions();
	}

}