package org.kesler.simplereg.logic.applicator;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.ULDAO;
import org.kesler.simplereg.dao.support.DAOException;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.support.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class ULService {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private List<UL> ulList;
	private List<UL> filteredULList;
	private String filterString;
	
	private final ULDAO ulDAO;

	private static ULService instance = null;

	public static synchronized ULService getInstance() {
		if (instance == null) {
			instance = new ULService();
		}
		return instance;
	}

	private ULService() {
		ulDAO = DAOFactory.getInstance().getULDAO();
		ulList = new ArrayList<UL>();
		filteredULList = new ArrayList<UL>();
		filterString = "";
	}


	public List<UL> getAllULs() {
		return ulList;
	}

    public List<UL> getULsByShortName(String shortName) {
        return ulDAO.getULsByShortName(shortName);
    }

    public List<UL> getULsByFullName(String fullName) {
        return ulDAO.getULsByFullName(fullName);
    }

	public void readULs() throws ServiceException {
		ulList = ulDAO.getAllItems();
	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterULs() {

		if (filterString!=null&&!filterString.isEmpty()) {
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

	public int addUL(UL ul) throws ServiceException {

		Long id = null;
		try {
			id = ulDAO.addItem(ul);
		} catch (DAOException e) {
			log.error("Error adding UL", e);
			throw new ServiceException("Error adding UL",e);
		}
		if (id != null) {
			ulList.add(ul);
			return ulList.size()-1;			
		} else {
			return -1;
		}

	}

	public void updateUL(UL ul) throws ServiceException {
		try {
			ulDAO.updateItem(ul);
		} catch (DAOException e) {
			log.error("Error updating UL", e);
			throw new ServiceException("Error updating UL",e);

		}
	}

	public void removeUL(UL ul) throws ServiceException {
		try {
			ulDAO.removeItem(ul);
			ulList.remove(ul);
		} catch (DAOException e) {
			log.error("Error removing UL", e);
			throw new ServiceException("Error removing UL",e);

		}
	}

}