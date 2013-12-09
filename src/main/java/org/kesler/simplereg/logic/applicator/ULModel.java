package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;

public class ULModel {
	private List<UL> ulList;
	private List<UL> filteredULList;
	private String filterString;

	private static ULModel instance = null;

	public static synchronized ULModel getInstance() {
		if (instance == null) {
			instance = new ULModel();
		}
		return instance;
	}

	private ULModel() {
		ulList = new ArrayList<UL>();
		filteredULList = new ArrayList<UL>();
		filterString = "";
	}


	public List<UL> getAllULs() {
		return ulList;
	}

	public void readFromDB() {
		ulList = DAOFactory.getInstance().getULDAO().getAllULs();
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterULs() {
		if (!filterString.isEmpty()) {
			filteredULList = new ArrayList<UL>();
			for (UL ul: ulList) {
				if (ul.getShortName().toLowerCase().indexOf(filterString.toLowerCase(),0) != -1) {
					filteredULList.add(ul);
				}
			}
		} else {
			filteredULList = ulList;
		}
	}

	public List<UL> getFilteredULs() {
		return filteredULList;
	}

	public int addUL(UL ul) {

		Long id = DAOFactory.getInstance().getULDAO().addUL(ul);
		if (id != null) {
			ulList.add(ul);
			return ulList.size()-1;			
		} else {
			return -1;
		}

	}

	public void updateUL(UL ul) {
		DAOFactory.getInstance().getULDAO().updateUL(ul);
	}

	public void deleteUL(UL ul) {
		DAOFactory.getInstance().getULDAO().deleteUL(ul);
		ulList.remove(ul);
	}


}