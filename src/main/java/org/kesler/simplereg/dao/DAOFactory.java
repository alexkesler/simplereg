package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.impl.ServiceDAOImpl;
import org.kesler.simplereg.dao.impl.OperatorDAOImpl;
import org.kesler.simplereg.dao.impl.ReceptionDAOImpl;
import org.kesler.simplereg.dao.impl.GenericDAOImpl;
import org.kesler.simplereg.dao.impl.FLDAOImpl;
import org.kesler.simplereg.dao.impl.ULDAOImpl;
import org.kesler.simplereg.dao.impl.ReceptionStatusDAOImpl;

import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.realty.RealtyObject;
import org.kesler.simplereg.logic.realty.RealtyType;

public class DAOFactory {
	private static ServiceDAO serviceDAO = null;
	private static GenericDAO<Operator> operatorDAO = null;
	private static ReceptionDAO receptionDAO = null;
	private static FLDAO flDAO = null;
	private static ULDAO ulDAO = null;
	private static ReceptionStatusDAO receptionStatusDAO = null;
	private static GenericDAO<RealtyObject> realtyObjectDAO = null;
	private static GenericDAO<RealtyType> realtyTypeDAO = null;
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


	public GenericDAO<Operator> getOperatorDAO() {
		if (operatorDAO == null) {
			operatorDAO = new GenericDAOImpl<Operator>(Operator.class);
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

	public ReceptionStatusDAO getReceptionStatusDAO() {
		if (receptionStatusDAO == null) {
			receptionStatusDAO = new ReceptionStatusDAOImpl();
		}
		return receptionStatusDAO;
	}

	public GenericDAO<RealtyObject> getRealtyObjectDAO() {
		if (realtyObjectDAO == null) {
			realtyObjectDAO = new GenericDAOImpl<RealtyObject>(RealtyObject.class);
		}
		
		return realtyObjectDAO;
	}

	public GenericDAO<RealtyType> getRealtyTypeDAO() {
		if (realtyTypeDAO == null) {
			realtyTypeDAO = new GenericDAOImpl<RealtyType>(RealtyType.class);
		}
		
		return realtyTypeDAO;
	}

}