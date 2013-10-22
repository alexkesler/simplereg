package org.kesler.simplereg.logic.realty;

import java.util.List;

public class RealtyObjectsModel {

	private static RealtyObjectsModel instance;

	private List<RealtyObect> realtyObjects;

	private RealtyObjectsModel() {}

	public static synchronized getInstance() {
		if (instance == null) {
			instance = new RealtyObjectsModel()
		}
		return instance;
	}


	public List<RealtyObect> getAllRealtyObjects() {
		if (realtyObjects == null) {
			readRealtyObjectsFromDB();
		}

		return realtyObjects;

	}

	private void readRealtyObjectsFromDB() {
		
	}


}