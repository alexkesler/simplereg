package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import org.kesler.simplereg.logic.applicator.UL;

public class TestULDAO {

	@Test
	public void testAddRead() {
		UL initUL = new UL();

		Long id = DAOFactory.getInstance().getULDAO().add(initUL);

		assertNotNull(id);

		UL resultUL = DAOFactory.getInstance().getULDAO().getULById(id);

		assertEquals(initUL, resultUL);

	}
}