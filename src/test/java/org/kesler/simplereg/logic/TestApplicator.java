package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestApplicator {

	@Test
	public void testCreate() {
		Applicator applicator = new Applicator();
		Long resultLong = applicator.getId();
		assertNull("Id must be null", resultLong);
	}

	@Test
	public void testCreateWIthFIO() {
		Applicator applicator = new Applicator("Иван", "Иванович", "Иванов");
		String resultFirstName = applicator.getFirstName();
		assertEquals("Error set firstName", "Иван", resultFirstName);
		String resultParentName = applicator.getParentName();
		assertEquals("Error set parentName", "Иванович", resultParentName);
		String resultSurName = applicator.getSurName();
		assertEquals("Error set surName", "Иванов", resultSurName);
	}

	@Test
	public void testSetGetFirstName() {
		Applicator applicator = new Applicator();
		String initFirstName = "Иван";
		applicator.setFirstName(initFirstName);
		String resultFirstName = applicator.getFirstName();
		assertEquals("FirstName set-get error", initFirstName, resultFirstName);
	}

	@Test
	public void testSetGetParentName() {
		Applicator applicator = new Applicator();
		String initParentName = "Иванович";
		applicator.setParentName(initParentName);
		String resultParentName = applicator.getParentName();
		assertEquals("ParentName set-get error", initParentName, resultParentName);
	}

	@Test
	public void testSetGetSurName() {
		Applicator applicator = new Applicator();
		String initSurName = "Иванов";
		applicator.setSurName(initSurName);
		String resultSurName = applicator.getSurName();
		assertEquals("SurName set-get error", initSurName, resultSurName);
	}

	@Test
	public void testGetFIO() {
		Applicator applicator = new Applicator();
		applicator.setFirstName("Иван");
		applicator.setParentName("Иванович");
		applicator.setSurName("Иванов");
		String resultFIO = applicator.getFIO();
		assertEquals("FIO dont get", "Иванов Иван Иванович", resultFIO);

	}

	@Test
	public void testGetFIOShort() {
		Applicator applicator = new Applicator();
		applicator.setFirstName("Иван");
		applicator.setParentName("Иванович");
		applicator.setSurName("Иванов");
		String resultFIOShort = applicator.getFIOShort();
		assertEquals("FIOShort dont get", "Иванов И.И.", resultFIOShort);

	}

}

