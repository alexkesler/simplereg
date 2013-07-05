package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;




public class TestReceptionsModel {

	@Test
	public void testCreate() {
		ArrayList<Reception> receptions = new ArrayList<Reception>();
		ReceptionsModel receptionsModel = new ReceptionsModel(receptions);
		List<Reception> resultList = receptionsModel.getReceptions();
		assertEquals("Wrong List of receptions", receptions, resultList);
	}

	@Test
	public void testCreateEmpty() {
		ReceptionsModel receptionsModel = new ReceptionsModel();
		List<Reception> resultList = receptionsModel.getReceptions();
		assertNotNull("List is null", resultList);
	}

	@Test
	public void testAddReception() {
		ReceptionsModel receptionsModel = new ReceptionsModel();
		Service service = new Service("Simple service");
//		Reception reception = new Reception();

	}

}