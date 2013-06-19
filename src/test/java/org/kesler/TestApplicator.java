package org.kesler;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestApplicator {

	@Test 
	public void testCreate() {
		Applicator applicator = new Applicator(216, "Иванов Иван Иванович");
		long resultLong = applicator.getId();
		assertEquals("Wrong id", 216, resultLong);
		String resultString = applicator.getFIO();
		assertEquals("Wrong fio", "Иванов Иван Иванович", resultString);
	}

}

