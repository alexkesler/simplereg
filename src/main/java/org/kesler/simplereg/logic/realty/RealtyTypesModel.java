package org.kesler.simplereg.logic.realty;

import java.util.List;

import org.kesler.simplereg.dao.DAOFactory;

public class RealtyTypesModel {

	private List<RealtyType> realtyTypes;

	private static RealtyTypesModel instance;

	public static synchronized RealtyTypesModel getInstance() {
		if (instance == null) {
			instance = new RealtyTypesModel();
		}
		return instance;
	}

	public List<RealtyType> getAllRealtyTypes() {
		DAOFactory.getInstance().getRealtyTypeDAO().getAllItems();
	}

	public int addRealtyType(RealtyType realtyType) {
		int index;

		return index;
	}

}