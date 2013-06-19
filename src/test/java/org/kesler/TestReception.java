package org.kesler;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class TestReception {

	@Test
	public void testCreate() {
		Date date = new Date();
		Service service = new Service("Service 1");
		Applicator applicator = new Applicator("Вовка");
		Operator operator = new Operator(23, "Adam Smith", "A.Smith", true, "multipass", false, false);
		Reception reception = new Reception(5, service, applicator, operator, date);
		long resultLong = reception.getId();
		assertEquals("Wrong Id", 5, resultLong);
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
