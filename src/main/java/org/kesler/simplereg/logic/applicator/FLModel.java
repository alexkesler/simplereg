package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;

public class FLModel {
	private List<FL> flList;
	private List<FL> filteredFLList;
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

	public void filterFLList(String filter) {
		
		// если строка фильтра не пустая - пересоздаем фильтрованный список
		if (!filter.isEmpty()) {
			filteredFLList = new ArrayList<FL>();
			for (FL fl: flList) {
				if (fl.getSurName().toLowerCase().indexOf(filter.toLowerCase(),0) == 0) {
					filteredFLList.add(fl);
				}
			}
		} else {
			filteredFLList = null;
		}

	}

	public List<FL> getFilteredFLs() {
		if (filteredFLList == null) {
			return getAllFLs();
		} else {
			return filteredFLList;
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