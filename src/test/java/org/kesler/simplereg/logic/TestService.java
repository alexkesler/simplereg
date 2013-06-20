package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestService {

	@Test
	public void testCreateByName() {
		Service service = new Service("Test");
		String resultString = service.getName();
		assertEquals("Wrong name", "Test", resultString);
		long resultLong = service.getId();
		assertEquals("default ID not 0",0,resultLong);
		Service resultService = service.getParentService();
		assertNull("default parentService must be Null", resultService);
		boolean resultBoolean = service.getEnabled();
		assertTrue("default enabled not true",resultBoolean);
	}

}