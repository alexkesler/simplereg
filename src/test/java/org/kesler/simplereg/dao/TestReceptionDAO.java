package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;


import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Reception;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.ReceptionDAO;

public class TestReceptionDAO {


	@Test
	public void testAddRead() {
		
		// создаем тестовый объект Service
		Service initService = new Service();

		// сохраняем
		try {
			DAOFactory.getInstance().getServiceDAO().addService(initService);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// создаем тестовый объект Applicator
		List<Applicator> initApplicators = new ArrayList<Applicator>();

		Applicator initApplicator = new ApplicatorFL();
		initApplicators.add(initApplicator);

		// сохраняем
/*
		try {
			DAOFactory.getInstance().getApplicatorDAO().addApplicator(a1);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}
*/
		// создаем тестовый объект Operator
		Operator initOperator = new Operator();
		initOperator.setState(Operator.NEW_STATE);
		List<Operator> initOperators = new ArrayList<Operator>();
		initOperators.add(initOperator);

		// сохраняем
		try {
			DAOFactory.getInstance().getOperatorDAO().saveOperators(initOperators);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// создаем тестовый объект Reception
		Reception initReception = new Reception(initService,initApplicators,initOperator,new Date());

		Reception resultReception = null;

		System.out.println("Writing Reception....");
		try {
			DAOFactory.getInstance().getReceptionDAO().addReception(initReception);
		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// получаем id сохраненнг объекта
		Long id = initReception.getId();

		// читаем объект
		try {
			resultReception = DAOFactory.getInstance().getReceptionDAO().getReceptionById(id);
		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		assertNotNull("Readed Reception is null", resultReception);

		// проверяем соответствие полей в членах объекта
		Service resultService = resultReception.getService();
		assertEquals("Service not the same", initService, resultService);

		Operator resultOperator = resultReception.getOperator();
		assertEquals("Operator not the same", initOperator, resultOperator);

		List<Applicator> resultApplicators = resultReception.getApplicators();
		assertNotNull("Readed applicators list is null",resultApplicators);

		assertTrue("Result applicators list not contains initApplicator",resultApplicators.contains(initApplicator));

	}


}