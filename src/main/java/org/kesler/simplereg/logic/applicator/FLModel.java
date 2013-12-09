package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;

public class FLModel {
	private List<FL> flList;
	private List<FL> filteredFLList;
	private String filterString;
	private static FLModel instance = null;

	public static synchronized FLModel getInstance() {
		if (instance == null) {
			instance = new FLModel();
		}
		return instance;
	}

	private FLModel() {

		flList = new ArrayList<FL>();
		filteredFLList = new ArrayList<FL>();
		filterString = "";

	}

	public List<FL> getAllFLs() {

		return flList;

	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterFLs() {
		
		// если строка фильтра не пустая - пересоздаем фильтрованный список
		if (!filterString.isEmpty()) {
			filteredFLList = new ArrayList<FL>();
			for (FL fl: flList) {
				if (fl.getSurName().toLowerCase().indexOf(filterString.toLowerCase(),0) == 0) {
					filteredFLList.add(fl);
				}
			}
		} else {
			filteredFLList = flList;
		}

	}

	public List<FL> getFilteredFLs() {

		return filteredFLList;
		
	}

	public int addFL(FL fl) {
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

	public void readFromDB() {
		flList = DAOFactory.getInstance().getFLDAO().getAllFLs();
	}

}