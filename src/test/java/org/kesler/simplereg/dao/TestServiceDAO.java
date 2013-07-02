package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;

import org.kesler.simplereg.logic.Service;

public class TestServiceDAO {

	@Test
	public void testWriteRead() {
		Service s1 = new Service();

		s1.setName("Service # 1");

		try{
			DAOFactory.getInstance().getServiceDAO().addService(s1);
		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());			
		}

		Long id = s1.getId();
		assertNotNull("Writed service dont get id", id);

		Service resultService = null;
		try {
			resultService = DAOFactory.getInstance().getServiceDAO().getServiceById(id);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		assertNotNull("Result service is null", resultService);


	}

	@Test
	public void testWriteReadWithParent() {
		Service s1 = new Service();
		Service s2 = new Service();
		Service s21 = new Service();

		s1.setName("Service # 1");
		s2.setName("Service # 2");
		s21.setName("Service # 21");
		s21.setParentService(s2);

		System.out.println("Writing Services....");
		try {
			DAOFactory.getInstance().getServiceDAO().addService(s1);
			DAOFactory.getInstance().getServiceDAO().addService(s2);
			DAOFactory.getInstance().getServiceDAO().addService(s21);

		} catch (SQLException sqle) {
			System.out.println("DB Error: " + sqle.getMessage());
		}

		Long id = s21.getId();
		assertNotNull("Writed service dont get id", id);
		
		if (id == null) {
			return; //если предыдущий тест провален - выходим
		}

		Service resultService = null;
		try {
			resultService = DAOFactory.getInstance().getServiceDAO().getServiceById(id);
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		}
		assertNotNull("Result service is null", resultService);

		if (resultService == null) {
			return; //если предыдущий тест провален - выходим
		}

		String parentServiceName = resultService.getParentServiceName();
		assertEquals("Parent service name don't equals", "Service # 2", parentServiceName);


	}

}