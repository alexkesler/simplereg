package org.kesler.simplereg.logic.applicator;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestUL {

	@Test
	public void testCreate() {
		UL ul = new UL();
		assertNotNull("UL default constructor not found", ul);
	} 

	@Test
	public void testSetGetFullName() {
		UL ul = new UL();
		String fullName = "Full name";
		ul.setFullName(fullName);
		String resultFullName = ul.getFullName();
		assertEquals("FullName get-set don't work", fullName, resultFullName);
	}

	@Test
	public void testSetGetShortName() {
		UL ul = new UL();
		String shortName = "Short name";
		ul.setShortName(shortName);
		String resultShortName = ul.getShortName();
		assertEquals("ShortName get-set don't work", shortName, resultShortName);
	}
}