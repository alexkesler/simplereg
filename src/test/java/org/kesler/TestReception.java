package org.kesler;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class TestReception {

	@Test
	public void testCreate() {
		Date date = new Date();
		Reception reception = new Reception(5, 23, "Adam Smith", date, 15);
		long resultLong = reception.getId();
		assertEquals("Wrong Id", 5, resultLong);
		resultLong = reception.getServiceId();
		assertEquals("Wrong ServiceId", 23, resultLong);
		String resultString = reception.getApplicatorFIO();
		assertEquals("Wrong applicatorFIO", "Adam Smith", resultString);
		Date resultDate = reception.getOpenDate();
		assertEquals("Wrong openDate", date, resultDate);
		resultLong = reception.getOperatorId();
		assertEquals("Wrong operatorId", 15, resultLong);


	}

} 
