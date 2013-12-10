package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestFL {

	@Test
	public void testCreate() {
		FL fl = new FL();
		assertNotNull("Don't create FL", fl);
	}

	@Test
	public void testSetGetFirstName() {
		FL fl = new FL();
		String firstName = "Анатолий";
		fl.setFirstName(firstName);
		String resultFirstName = fl.getFirstName();
		assertEquals("FirstName not set", firstName, resultFirstName);
	}

	@Test
	public void testSetGetParentName() {
		FL fl = new FL();
		String parentName = "Петрович";
		fl.setParentName(parentName);
		String resultParentName = fl.getParentName();
		assertEquals("ParentName not set", parentName, resultParentName);
	}

	@Test
	public void testSetGetSurName() {
		FL fl = new FL();
		String surName = "Иванов";
		fl.setSurName(surName);
		String resultSurName = fl.getSurName();
		assertEquals("SurName not set", surName, resultSurName);
	}
}