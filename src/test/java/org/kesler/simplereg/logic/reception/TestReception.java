package org.kesler.simplereg.logic.reception;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;
import org.junit.Ignore;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.applicator.Applicator;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.Service;

public class TestReception {

	@Test
	public void testCreate() {
		Reception reception = new Reception();
		assertNotNull("Don't create reception", reception);
	}


	@Test
	public void testCreateWithParam() {
		// Готовим параметры
		Date date = new Date();
		Service service = new Service();
		List<Applicator> applicators = new ArrayList<Applicator>();
		Operator operator = new Operator();
		// Создаем прием
		Reception reception = new Reception(service, applicators, operator, date);
		// Проверяем записанные данные
		Service resultService = reception.getService();
		assertEquals("Wrong Service", service, resultService);
		List<Applicator> resultApplicators = reception.getApplicators();
		assertEquals("Wrong applicators", applicators, resultApplicators);
		Operator resultOperator = reception.getOperator();
		assertEquals("Wrong operator", operator, resultOperator);
		Date resultDate = reception.getOpenDate();
		assertEquals("Wrong openDate", date, resultDate);
	}	
	
} 
