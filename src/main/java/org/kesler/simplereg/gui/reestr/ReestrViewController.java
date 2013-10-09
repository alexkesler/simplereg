package org.kesler.simplereg.gui.reestr;

import java.util.List;

import org.kesler.simplereg.logic.reception.Reception;
import org.kesler.simplereg.logic.reception.ReceptionsModel;

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

	List<Reception> getReceptions() {
		return model.getReceptions();
	}

	public void openFilterDialog() {
		ReestrFilterDialog filterDialog = new ReestrFilterDialog(view);
		filterDialog.setVisible(true);
	}

	public void openColumnsDialog() {
		ReestrColumnsDialog reestrColumnsDialog  = new ReestrColumnsDialog(view);
		reestrColumnsDialog.setVisible(true);
	}

	public void applyFilter() {

	}

	public void resetFilter() {

	}
}