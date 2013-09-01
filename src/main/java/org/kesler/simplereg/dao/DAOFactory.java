package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.impl.ServiceDAOImpl;
import org.kesler.simplereg.dao.impl.OperatorDAOImpl;
import org.kesler.simplereg.dao.impl.ReceptionDAOImpl;
import org.kesler.simplereg.dao.impl.FLDAOImpl;
import org.kesler.simplereg.dao.impl.ULDAOImpl;

public class DAOFactory {
	private static ServiceDAO serviceDAO = null;
	private static OperatorDAO operatorDAO = null;
	private static ReceptionDAO receptionDAO = null;
	private static FLDAO flDAO = null;
	private static ULDAO ulDAO = null;
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

	public FLDAO getFLDAO() {
		if (flDAO == null) {
			flDAO = new FLDAOImpl();
		}
		return flDAO;
	}

	public ULDAO getULDAO() {
		if (ulDAO == null) {
			ulDAO = new ULDAOImpl();
		}
		return ulDAO;
	}

}