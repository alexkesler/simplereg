package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

import org.kesler.simplereg.logic.applicator.FL;

public class TestFLDAO {

	@Test
	public void testAddRead() {

		FL initFL = new FL();
		DAOFactory.getInstance().getFLDAO().addFL(initFL);

		Long id = initFL.getId();

		assertNotNull("Id not set", id);

		FL resultFL = new FL();
		resultFL = DAOFactory.getInstance().getFLDAO().getFLById(id);

		assertNotNull(resultFL);

	}

	@Test
	public void testAddGetList() {
		FL initFL1 = new FL();
		initFL1.setFirstName("Иван");
		FL initFL2 = new FL();
		initFL2.setFirstName("Петр");

		DAOFactory.getInstance().getFLDAO().addFL(initFL1);
		DAOFactory.getInstance().getFLDAO().addFL(initFL2);

		List<FL> flList = DAOFactory.getInstance().getFLDAO().getAllFLs();

		assertTrue("FLlist not contains stored FL",flList.contains(initFL1));
		assertTrue("FLlist not contains stored FL",flList.contains(initFL2));
	}

	@Test
	public void testDelete() {
		FL initFL = new FL();

		DAOFactory.getInstance().getFLDAO().addFL(initFL);

		DAOFactory.getInstance().getFLDAO().delete(initFL);

		List<FL> list = DAOFactory.getInstance().getFLDAO().getAllFLs();

		assertFalse(list.contains(initFL));


	}
	
}