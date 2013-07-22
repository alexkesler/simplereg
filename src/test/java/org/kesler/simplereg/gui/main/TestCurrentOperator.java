package org.kesler.simplereg.gui.main;

import static org.junit.Assert.*;
import org.junit.Test;

import org.kesler.simplereg.logic.Operator;

public class TestCurrentOperator {

	@Test
	public void testSetGetOperator() {
		Operator nullOperator = CurrentOperator.getOperator();
		assertNull("Initial Operator not null",nullOperator);

		Operator initOperator = new Operator();
		CurrentOperator.setOperator(initOperator);
		Operator resultOperator = CurrentOperator.getOperator();
		assertEquals("Operator not set", initOperator, resultOperator);
	}	

	@Test
	public void testResetOperator() {
		Operator initOperator = new Operator();
		CurrentOperator.setOperator(initOperator);

		CurrentOperator.resetOperator();
		Operator nullOperator = CurrentOperator.getOperator();
		assertNull("ResetOperator don'n set Operator to null", nullOperator);
	}

}