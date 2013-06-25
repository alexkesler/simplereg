package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.impl.ServiceDAOImpl;
import org.kesler.simplereg.dao.impl.ApplicatorDAOImpl;

public class DAOFactory {
	private static ServiceDAO serviceDAO = null;
	private static ApplicatorDAO applicatorDAO = null;
	private static DAOFactory instance = null;

	public static synchronized DAOFactory getInstance() {
		if (instance == null) {	
			instance = new DAOFactory();
		}
		return instance;
	}

	public ServiceDAO getServiceDAO() {
		if (serviceDAO == null) {
			serviceDAO = new ServiceDAOImpl();
		}
		return serviceDAO;
	}

	public ApplicatorDAO getApplicatorDAO() {
		if (applicatorDAO == null) {
			applicatorDAO = new ApplicatorDAOImpl();
		}
		return applicatorDAO;
	}

}