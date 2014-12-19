package org.kesler.simplereg.logic.realty;

import java.util.List;

import org.kesler.simplereg.dao.DAOFactory;

public class RealtyTypesService {

	private List<RealtyType> realtyTypes;

	private static RealtyTypesService instance;

	public static synchronized RealtyTypesService getInstance() {
		if (instance == null) {
			instance = new RealtyTypesService();
		}
		return instance;
	}

	public List<RealtyType> getAllRealtyTypes() {
		if (realtyTypes == null) {
			readRealtyTypesFromDB();
		}
		
		return realtyTypes;
	}

	public void readRealtyTypesFromDB() {
		realtyTypes = DAOFactory.getInstance().getRealtyTypeDAO().getAllItems();
	} 

	public int addRealtyType(RealtyType realtyType) {
		int index = -1;

		DAOFactory.getInstance().getRealtyTypeDAO().addItem(realtyType);
		realtyTypes.add(realtyType);
		index = realtyTypes.size()-1;

		return index;
	}

	public void updateRealtyType(RealtyType realtyType) {
		DAOFactory.getInstance().getRealtyTypeDAO().updateItem(realtyType);
	}

	public void removeRealtyType(RealtyType realtyType) {
		DAOFactory.getInstance().getRealtyTypeDAO().removeItem(realtyType);
		realtyTypes.remove(realtyType);
	}

}