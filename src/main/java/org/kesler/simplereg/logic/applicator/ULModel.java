package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.dao.DAOFactory;

public class ULModel {
	private List<UL> ulList;
	private List<UL> filteredULList;

	private static ULModel instance = null;

	public static synchronized ULModel getInstance() {
		if (instance == null) {
			instance = new ULModel();
		}
		return instance;
	}

	private ULModel() {}


	public List<UL> getAllULs() {
		if (ulList == null) {
			readFromDB();
		}
		return ulList;
	}

	public void filterULList(String filterString) {
		if (!filterString.isEmpty()) {
			filteredULList = new ArrayList<UL>();
			for (UL ul: ulList) {
				if (ul.getShortName().toLowerCase().indexOf(filterString.toLowerCase(),0) != -1) {
					filteredULList.add(ul);
				}
			}
		} else {
			filteredULList = null;
		}
	}

	public List<UL> getFilteredULs() {
		if (filteredULList == null) {
			return getAllULs();
		} else {
			return filteredULList;
		}
	}

	public int addUL(UL ul) {
		if (ulList == null) {
			readFromDB();
		}

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


	private void readFromDB() {
		ulList = DAOFactory.getInstance().getULDAO().getAllULs();
	}


}