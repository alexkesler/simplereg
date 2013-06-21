package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestService {

	@Test
	public void testCreateByName() {
		Service service = new Service("Test");
		String resultString = service.getName();
		assertEquals("Wrong name", "Test", resultString);
		Long resultLong = service.getId();
		assertNull("default id not Null", resultLong);
		Service resultService = service.getParentService();
		assertNull("default parentService must be Null", resultService);
		Boolean resultBoolean = service.getEnabled();
		assertTrue("default enabled not true",resultBoolean);
	}

}