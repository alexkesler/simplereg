package org.kesler;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestOperator {

	@Test
	public void testCreate() {
		Operator operator = new Operator(1,"Adam Smith", "A.Smith", true, "multipass", false, false);
		long resultLong = operator.getId();
		assertEquals("Wrong ID", 1, resultLong);
		String resultString = operator.getFIO();
		assertEquals("Wronf FIO", "Adam Smith", resultString);
		resultString = operator.getFIOShort();
		assertEquals("Wrong FIO Short", "A.Smith",resultString);
		boolean resultBoolean = operator.getEnabled();
		assertTrue("Wrong enabled",resultBoolean);
		resultString = operator.getPassword();
		assertEquals("Wrong password", "multipass", resultString);
		resultBoolean = operator.getIsKontroler();
		assertFalse("Wrong IsKontroler", resultBoolean);
		resultBoolean = operator.getIsAdmin();
		assertFalse("Wrong IsAdmin", resultBoolean);		
	}


	@Test 
	public void testCreateByName() {
		Operator operator = new Operator("Adam Smith");
		String resultString = operator.getFIO();
		assertEquals("Wrong FIO", "Adam Smith", resultString);
	}


}