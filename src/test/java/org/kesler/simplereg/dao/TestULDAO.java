package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

import org.kesler.simplereg.logic.applicator.UL;

public class TestULDAO {

	@Test
	public void testAddRead() {
		UL initUL = new UL();

		Long id = DAOFactory.getInstance().getULDAO().addUL(initUL);
		assertNotNull(id);

		UL resultUL = DAOFactory.getInstance().getULDAO().getULById(id);
		assertEquals(initUL, resultUL);

	}

	@Test
	public void testGetAllULs() {

		UL initUL = new UL();

		DAOFactory.getInstance().getULDAO().addUL(initUL);

		List<UL> resultList = DAOFactory.getInstance().getULDAO().getAllULs();
		assertTrue(resultList.contains(initUL));

	}

	@Test
	public void testDelete() {
		UL initUL = new UL();

		DAOFactory.getInstance().getULDAO().addUL(initUL);

		DAOFactory.getInstance().getULDAO().deleteUL(initUL);

		List<UL> list = DAOFactory.getInstance().getULDAO().getAllULs();
		assertFalse(list.contains(initUL));
	}
}