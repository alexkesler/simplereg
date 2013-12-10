package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import org.kesler.simplereg.logic.Operator;

public class TestDAOFactory{

	@Test
	public void testGetInstance() {
		DAOFactory daoFactory = DAOFactory.getInstance();
		assertNotNull("DAOFactory not returned", daoFactory);

		DAOFactory daoFactory1 = DAOFactory.getInstance();
		assertEquals("DAOFactory singleton error", daoFactory, daoFactory1);
	}

	@Test
	public void testGetServiceDAO() {

		ServiceDAO serviceDAO = DAOFactory.getInstance().getServiceDAO();
		assertNotNull("ServiceDAO not returned", serviceDAO);
	}

	@Test
	public void testGetOperatorDAO() {

		GenericDAO<Operator> operatorDAO = DAOFactory.getInstance().getOperatorDAO();
		assertNotNull("OperatorDAO not returned", operatorDAO);

	}

	@Test
	public void testGetReceptionDAO() {

		ReceptionDAO receptionDAO = DAOFactory.getInstance().getReceptionDAO();
		assertNotNull("ReceptionDAO not returned");

	}

	@Test
	public void testGetFLDAO() {

		FLDAO flDAO = DAOFactory.getInstance().getFLDAO();
		assertNotNull("FLDAO not returned", flDAO);
		
	}

	@Test
	public void testGetULDAO() {

		ULDAO ulDAO = DAOFactory.getInstance().getULDAO();
		assertNotNull("ULDAO not returned", ulDAO);
		
	}
}