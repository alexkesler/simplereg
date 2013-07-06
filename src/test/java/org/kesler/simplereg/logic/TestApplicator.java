package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestApplicator {

	@Test
	public void testCreateByFIO() {
		Applicator applicator = new Applicator("Вова Петров");
		Long resultLong = applicator.getId();
		assertNull("Id must be null", resultLong);
		String resultString = applicator.getFIO();
		assertEquals("Wrong fio", "Вова Петров", resultString);
	}

}

