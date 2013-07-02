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

	@Test
	public void testParentService() {
		Service s1 = new Service("Service #1");
		Service s11 = new Service("Service #11");
		s11.setParentService(s1);
		String resultString = s11.getParentServiceName();
		assertEquals("Parent Service not set", "Service #1", resultString);
	}

	@Test
	public void testGetParentNameWithNoParentService() {
		Service s1 = new Service("Service # 1");
		String mustBeString = "Родительская услуга не определена";
		String resultString = s1.getParentServiceName();
		assertEquals("No ParentService String not set properly", mustBeString, resultString);
	}
}