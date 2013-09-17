package org.kesler.simplereg.applicator;

import java.util.List;

import org.kesler.simplereg.logic.applicator.FL;
import org.kesler.simplereg.dao.DAOFactory;

public class FLModel {
	private List<FL> flList;
	private static FLModel instance = null;

	public static synchronized FLModel getInstance() {
		if (instance == null) {
			instance = new FLModel();
		}
		return instance;
	}

	private FLModel() {

	}

	public List<FL> getAllFL() {
		if (flList == null) {
			readFromDB();
		}
		return flList;
	}

	private void readFromDB() {
		flList = DAOFactory.getInstance().getFLDAO().getAllFLs();
	}

}