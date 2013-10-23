package org.kesler.simplereg.logic.realty;

import java.util.List;

import org.kesler.simplereg.dao.DAOFactory;

public class RealtyObjectsModel {

	private static RealtyObjectsModel instance;

	private List<RealtyObject> realtyObjects;

	private RealtyObjectsModel() {}

	public static synchronized RealtyObjectsModel getInstance() {
		if (instance == null) {
			instance = new RealtyObjectsModel();
		}
		return instance;
	}


	public List<RealtyObject> getAllRealtyObjects() {
		if (realtyObjects == null) {
			readRealtyObjectsFromDB();
		}

		return realtyObjects;

	}

	public void readRealtyObjectsFromDB() {
		
		realtyObjects = DAOFactory.getInstance().getRealtyObjectDAO().getAllItems();

	}


}