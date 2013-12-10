package org.kesler.simplereg.logic.applicator;

import static org.junit.Assert.*;
import org.junit.Test;

import org.kesler.simplereg.logic.Applicator;
import org.kesler.simplereg.logic.FL;
import org.kesler.simplereg.logic.UL;

public class TestApplicatorFL {

	@Test
	public void testCreate() {
		ApplicatorFL applicatorFL = new ApplicatorFL();
		assertNotNull("Default constructor not set", applicatorFL);
	} 

	@Test
	public void testCreateByApplicator() {
		Applicator applicator = new ApplicatorFL();
		assertNotNull("Error extending Appliator", applicator);
	}

	@Test
	public void testSetGetFL() {
		ApplicatorFL applicatorFL = new ApplicatorFL();
		FL initFL = new FL();
		applicatorFL.setFL(initFL);
		FL resultFL  = applicatorFL.getFL();
		assertEquals("Error set-get FL", initFL, resultFL);
	}

	@Test
	public void testSetGetRepres() {
		ApplicatorFL applicatorFL = new ApplicatorFL();
		FL initRepres = new FL();
		applicatorFL.setRepres(initRepres);
		FL resultRepres = applicatorFL.getRepres();
		assertEquals("Error set-get Repres", initRepres, resultRepres);
	}
}