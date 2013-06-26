package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;

import java.sql.SQLException;

import org.kesler.simplereg.dao.impl.ServiceDAOImpl;
import org.kesler.simplereg.logic.Service;

public class TestServiceDAO {

	private ServiceDAO serviceDAO = new ServiceDAOImpl();

	@Before
	public void prepareTable() {

	}

	@Ignore
	@Test
	public void testWriteRead() {
		Service service = new Service("Test service");
		try {
			serviceDAO.addService(service);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		Long id = service.getId();
		assertNotNull("Writed service dont get id", id);
		Service resultService = null;
		try {
			resultService = serviceDAO.getServiceById(id);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		assertEquals("Don't equals", service, resultService);
	}

}