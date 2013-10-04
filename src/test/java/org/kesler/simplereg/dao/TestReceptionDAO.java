package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.applicator.ApplicatorFL;
import org.kesler.simplereg.logic.applicator.ApplicatorUL;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.reception.Reception;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.ReceptionDAO;

public class TestReceptionDAO {
	@Test
	public void testAddRead() {
		
		// создаем тестовый объект Service
		Service initService = new Service();

		// сохраняем
		DAOFactory.getInstance().getServiceDAO().addService(initService);

		// создаем тестовые объекты Applicator
		List<Applicator> initApplicators = new ArrayList<Applicator>();

		Applicator initApplicatorFL = new ApplicatorFL();
		initApplicators.add(initApplicatorFL);
		Applicator initApplicatorUL = new ApplicatorUL();
		initApplicators.add(initApplicatorUL);

		// создаем тестовый объект Operator
		Operator initOperator = new Operator();
		initOperator.setState(EntityState.NEW);
		List<Operator> initOperators = new ArrayList<Operator>();
		initOperators.add(initOperator);

		// сохраняем
		DAOFactory.getInstance().getOperatorDAO().saveOperators(initOperators);

		// создаем тестовый объект Reception
		Reception initReception = new Reception();

		initReception.setService(initService);
		initReception.setApplicators(initApplicators);
		initReception.setOperator(initOperator);
		initReception.setOpenDate(new Date());

		Reception resultReception = null;

		System.out.println("Writing Reception....");
		DAOFactory.getInstance().getReceptionDAO().addReception(initReception);

		// получаем id сохраненнг объекта
		Long id = initReception.getId();

		// читаем объект
		resultReception = DAOFactory.getInstance().getReceptionDAO().getReceptionById(id);

		assertNotNull("Readed Reception is null", resultReception);

		// проверяем соответствие полей в членах объекта
		Service resultService = resultReception.getService();
		assertEquals("Service not the same", initService, resultService);

		Operator resultOperator = resultReception.getOperator();
		assertEquals("Operator not the same", initOperator, resultOperator);

		List<Applicator> resultApplicators = resultReception.getApplicators();
		assertNotNull("Readed applicators list is null",resultApplicators);

		System.out.println("initApplicator UUID = " + initApplicatorFL.getUUID());

		System.out.println("result operators size = " + resultApplicators.size());
		for (Applicator a : resultApplicators) {
			System.out.println("resultApplicator UUID" + a.getUUID());
		}

		assertTrue("Result applicators list not contains initApplicator",resultApplicators.contains(initApplicatorFL));

	}


}