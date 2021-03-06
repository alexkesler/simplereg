package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.impl.*;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.UL;
import org.kesler.simplereg.logic.RealtyObject;
import org.kesler.simplereg.logic.reception.ReceptionStatus;
import org.kesler.simplereg.logic.realty.RealtyType;
import org.kesler.simplereg.util.Counter;

public class DAOFactory {
	private static ServiceDAO serviceDAO = null;
	private static GenericDAO<Operator> operatorDAO = null;
	private static ReceptionDAO receptionDAO = null;
	private static FLDAO flDAO = null;
	private static ULDAO ulDAO = null;
	private static GenericDAO<ReceptionStatus> receptionStatusDAO = null;
	private static ReceptionStatusChangeDAO receptionStatusChangeDAO = null;
	private static GenericDAO<RealtyObject> realtyObjectDAO = null;
	private static GenericDAO<RealtyType> realtyTypeDAO = null;
	private static GenericDAO<Counter> counterDAO = null;
    private static FIASRecordDAO fiasRecordDAO = null;
	private static TemplateDAO templateDAO = null;
	
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

	public GenericDAO<ReceptionStatus> getReceptionStatusDAO() {
		if (receptionStatusDAO == null) {
			receptionStatusDAO = new GenericDAOImpl<ReceptionStatus>(ReceptionStatus.class);
		}
		return receptionStatusDAO;
	}

	public static ReceptionStatusChangeDAO getReceptionStatusChangeDAO() {
		if (receptionStatusChangeDAO == null) {
			receptionStatusChangeDAO = new ReceptionStatusChangeDAOImpl();
		}
		return receptionStatusChangeDAO;
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

	public GenericDAO<Counter> getCounterDAO() {
		if (counterDAO == null) {
			counterDAO = new GenericDAOImpl<Counter>(Counter.class);
		}
		
		return counterDAO;
	}

    public FIASRecordDAO getFiasRecordDAO() {
        if (fiasRecordDAO == null) {
            fiasRecordDAO = new FIASRecordDAOImpl();
        }
        return fiasRecordDAO;
    }

	public TemplateDAO getTemplateDAO() {
		if (templateDAO==null) {
			templateDAO = new TemplateDAOImpl();
		}
		return templateDAO;
	}


}