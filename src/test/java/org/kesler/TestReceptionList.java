package org.kesler;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;
import java.util.ArrayList;


public class TestReceptionList {

	@Test
	public void testCreate() {
		ArrayList<Reception> receptions = new ArrayList<Reception>();
		ReceptionList receptionList = new ReceptionList(receptions);
		List<Reception> resultList = receptionList.getReceptions();
		assertEquals("Wrong List of receptions", receptions, resultList);
	}

	@Test
	public void testCreateEmpty() {
		ReceptionList receptionList = new ReceptionList();
		List<Reception> resultList = receptionList.getReceptions();
		assertNotNull("List is null", resultList);
	}

	@Test
	public void testAddReception() {
		ReceptionList receptionList = new ReceptionList();
		Service service = new Service("Simple service");
//		Reception reception = new Reception();

	}

}