package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestOperator {

	@Test
	public void testCreate() {
		Operator operator = new Operator("Adam Smith", "A.Smith", "multipass", false, false, true);
		String resultString = operator.getFIO();
		assertEquals("Wronf FIO", "Adam Smith", resultString);
		resultString = operator.getFIOShort();
		assertEquals("Wrong FIO Short", "A.Smith",resultString);
		boolean resultBoolean = operator.getEnabled();
		assertTrue("Wrong enabled",resultBoolean);
		resultString = operator.getPassword();
		assertEquals("Wrong password", "multipass", resultString);
		resultBoolean = operator.getIsControler();
		assertFalse("Wrong IsControler", resultBoolean);
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