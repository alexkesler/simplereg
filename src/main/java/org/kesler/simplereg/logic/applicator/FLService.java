package org.kesler.simplereg.logic.applicator;

import java.util.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.FLDAO;
import org.kesler.simplereg.dao.support.DAOException;

import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.support.ServiceException;

public class FLService{
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	private List<FL> flList;
	private List<FL> filteredFLList;
	private String filterString;
	private static FLService instance = null;
	private final FLDAO flDAO;


	public static synchronized FLService getInstance() {
		if (instance == null) {
			instance = new FLService();
		}
		return instance;
	}

	private FLService() {

		flList = new ArrayList<FL>();
		filteredFLList = new ArrayList<FL>();
		filterString = "";
		flDAO = DAOFactory.getInstance().getFLDAO();

	}


	public List<FL> getAllFLs() {

		return flList;

	}

	public void setFilterString(String filterString) {
		this.filterString = filterString;
	}

	public void filterFLs() {
		
		// если строка фильтра не пустая - пересоздаем фильтрованный список
		if (filterString!=null && !filterString.isEmpty()) {
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


    public List<FL> getFLsByFIO(String firstName, String surName, String parentName) throws ServiceException{
		log.info("ReadingFLs by FIO: " + surName +" "+firstName+" "+parentName);
		List<FL> fls = null;
		try {
			fls = flDAO.getFLByFIO(firstName, surName, parentName);
			log.info("Read "+fls.size()+ " FLs");
		} catch (DAOException e) {
			log.error("Error getting FLs by FIO", e);
			throw new ServiceException("Error getting FLs by FIO",e);
		}
		return fls;
    }


    public int addFL(FL fl) throws ServiceException{
		log.info("Add FL: " + fl.getFIO());
		Long id = null;
		try {
			id = flDAO.addItem(fl);
			log.info("Adding complete");
		} catch (DAOException e) {
			log.error("Error adding FL", e);
			throw new ServiceException("Error adding FL",e);
		}
		if (id != null) {
			flList.add(fl);
			return flList.size()-1;
		} else {
			return -1;
		}
		
	}

	public void updateFL(FL fl) throws ServiceException{
		log.info("Updating FL: " + fl.getFIO());
		try {
			flDAO.updateItem(fl);
			log.info("Updating complete");
		} catch (DAOException e) {
			log.error("Error updating FL", e);
			throw new ServiceException("Error updating FL",e);
		}
	}

	public void removeFL(FL fl) throws ServiceException{
		log.info("Removing FL: " + fl.getFIO());
		try {
			flDAO.removeItem(fl);
			flList.remove(fl);
			log.info("Removing complete");
		} catch (DAOException e) {
			log.error("Error removing FL", e);
			throw new ServiceException("Error removing FL",e);
		}
	}

	public void readLFs() throws ServiceException{
		log.info("Reading FLs..");
		try {
			flList = flDAO.getAllItems();
			log.info("Read " + flList.size() + " FLs");
		} catch (DAOException e) {
			log.error("Error reading FLs",e);
			throw new ServiceException("Error reading FLs",e);
		}
	}


}