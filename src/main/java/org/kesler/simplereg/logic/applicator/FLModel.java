package org.kesler.simplereg.logic.applicator;

import java.util.List;

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

	public List<FL> getAllFLs() {
		if (flList == null) {
			readFromDB();
		}
		return flList;
	}

	public void addFL(FL fl) {
		if (flList == null) {
			readFromDB();
		}
		Long id = DAOFactory.getInstance().getFLDAO().addFL(fl);
		if (id != null) {
			flList.add(fl);
		}
		
	}

	public void updateFL(FL fl) {
		// Написать обработчик
	}

	private void readFromDB() {
		flList = DAOFactory.getInstance().getFLDAO().getAllFLs();
	}

}