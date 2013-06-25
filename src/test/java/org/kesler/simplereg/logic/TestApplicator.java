package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestApplicator {

	@Test 
	public void testCreate() {
		Long initId = 216L;
		Applicator applicator = new Applicator(initId, "Иванов Иван Иванович");
		Long resultLong = applicator.getId();
		assertNotNull("Id is Null, but must not",resultLong);
		assertEquals("Wrong id", initId, resultLong);
		String resultString = applicator.getFIO();
		assertEquals("Wrong fio", "Иванов Иван Иванович", resultString);
	}

	@Test
	public void testCreateByName() {
		Applicator applicator = new Applicator("Вова Петров");
		Long resultLong = applicator.getId();
		assertNull("Id must be null", resultLong);
		String resultString = applicator.getFIO();
		assertEquals("Wrong fio", "Вова Петров", resultString);
	}

}

