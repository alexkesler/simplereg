package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class TestReception {

	@Test
	public void testCreate() {
		Date date = new Date();
		Service service = new Service("Service 1");
		Applicator applicator = new Applicator("Иван", "Иванович", "Иванов");
		Operator operator = new Operator("Adam Smith", "A.Smith", "multipass", false, false, true);
		Reception reception = new Reception(service, applicator, operator, date);
		Service resultService = reception.getService();
		assertEquals("Wrong Service", service, resultService);
		Applicator resultApplicator = reception.getApplicator();
		assertEquals("Wrong applicator", applicator, resultApplicator);
		Operator resultOperator = reception.getOperator();
		assertEquals("Wrong operator", operator, resultOperator);
		Date resultDate = reception.getOpenDate();
		assertEquals("Wrong openDate", date, resultDate);
	}	
	
} 
