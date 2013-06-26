package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.impl.ServiceDAOImpl;
import org.kesler.simplereg.dao.impl.ApplicatorDAOImpl;
import org.kesler.simplereg.dao.impl.OperatorDAOImpl;
import org.kesler.simplereg.dao.impl.ReceptionDAOImpl;

public class DAOFactory {
	private static ServiceDAO serviceDAO = null;
	private static ApplicatorDAO applicatorDAO = null;
	private static OperatorDAO operatorDAO = null;
	private static ReceptionDAO receptionDAO = null;
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

	public OperatorDAO getOperatorDAO() {
		if (operatorDAO == null) {
			operatorDAO = new OperatorDAOImpl();
		}
		return operatorDAO;
	}

	public ReceptionDAO getReceptionDAO() {
		if (receptionDAO == null) {
			receptionDAO = new ReceptionDAOImpl();
		}
		return receptionDAO;
	}


}