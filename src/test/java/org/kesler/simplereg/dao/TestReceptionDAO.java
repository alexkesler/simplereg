package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;


import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.kesler.simplereg.logic.Service;
import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.Reception;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.ReceptionDAO;

public class TestReceptionDAO {

	@Test
	public void testAddRead() {
		
		// создаем тестовый объект Service
		Service s1 = new Service();

		s1.setName("Service # 1");

		// сохраняем
		try {
			DAOFactory.getInstance().getServiceDAO().addService(s1);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// создаем тестовый объект Applicator
		Applicator a1 = new Applicator("Янус", "Полуэктович", "Невструев");

		// сохраняем
		try {
			DAOFactory.getInstance().getApplicatorDAO().addApplicator(a1);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// создаем тестовый объект Operator
		Operator o1 = new Operator("Операторов Оператор");
		o1.setState(Operator.NEW_STATE);
		List<Operator> os = new ArrayList<Operator>();
		os.add(o1);

		// сохраняем
		try {
			DAOFactory.getInstance().getOperatorDAO().saveOperators(os);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// создаем тестовый объект Reception
		Reception r1 = new Reception(s1,a1,o1,new Date());

		Reception resultReception = null;

		System.out.println("Writing Reception....");
		try {
			DAOFactory.getInstance().getReceptionDAO().addReception(r1);

		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		// получаем id сохраненнг объекта
		Long id = r1.getId();

		// читаем объект
		try {
			resultReception = DAOFactory.getInstance().getReceptionDAO().getReceptionById(id);
		} catch (SQLException e) {
			System.out.println("DB Error: " + e.getMessage());
		}

		assertNotNull("Readed Reception is null", resultReception);

		// проверяем соответствие полей в членах объекта
		String serviceName = resultReception.getServiceName();
		assertEquals("ServiceName not same", "Service # 1",serviceName);

		String applicatorFIO = resultReception.getApplicatorFIO();
		assertEquals("ApplicatorFIO not same", "Невструев Янус Полуэктович", applicatorFIO);

		String operatorFIO = resultReception.getOperatorFIO();
		assertEquals("OperatorFIO not same", "Операторов Оператор", operatorFIO);

	}


}