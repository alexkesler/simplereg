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

	public List<FL> getFilteredFLs(String filter) {
		List<FL> list = getAllFLs();
		List<FL> filteredList = new ArrayList<FL>();
		// если строка фильтра пустая - возвращаем полный список
		if (filter.isEmpty()) {
			return filteredList;
		} else {
			for (FL fl: list) {
				if (fl.getSurName().substring(0,filter.length-1).toLowerCase().equals(filter.toLowerCase())) {
					filteredList.add(fl);
				}
			}
			return filteredList;
		}

	}

	public int addFL(FL fl) {
		if (flList == null) {
			readFromDB();
		}
		Long id = DAOFactory.getInstance().getFLDAO().addFL(fl);
		if (id != null) {
			flList.add(fl);
			return flList.size()-1;
		} else {
			return -1;
		}
		
	}

	public void updateFL(FL fl) {
		DAOFactory.getInstance().getFLDAO().updateFL(fl);
	}

	public void deleteFL(FL fl) {
		DAOFactory.getInstance().getFLDAO().deleteFL(fl);
		flList.remove(fl);
	}

	private void readFromDB() {
		flList = DAOFactory.getInstance().getFLDAO().getAllFLs();
	}

}