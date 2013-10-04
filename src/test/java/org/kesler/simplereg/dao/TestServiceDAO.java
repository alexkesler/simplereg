package org.kesler.simplereg.dao;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Service;

public class TestServiceDAO {

	@Test
	public void testAddRead() {
		Service s1 = new Service();

		s1.setName("Service # 1");

		DAOFactory.getInstance().getServiceDAO().addService(s1);

		Long id = s1.getId();
		assertNotNull("Writed service dont get id", id);

		Service resultService = null;
		resultService = DAOFactory.getInstance().getServiceDAO().getServiceById(id);
		assertNotNull("Result service is null", resultService);


	}

	@Test
	public void testAddeadWithParent() {
		Service s1 = new Service();
		Service s2 = new Service();
		Service s21 = new Service();

		s1.setName("Service # 1");
		s2.setName("Service # 2");
		s21.setName("Service # 21");
		s21.setParentService(s2);

		System.out.println("Writing Services....");
		DAOFactory.getInstance().getServiceDAO().addService(s1);
		DAOFactory.getInstance().getServiceDAO().addService(s2);
		DAOFactory.getInstance().getServiceDAO().addService(s21);

		Long id = s21.getId();
		assertNotNull("Writed service dont get id", id);
		
		Service resultService = null;
		resultService = DAOFactory.getInstance().getServiceDAO().getServiceById(id);
		assertNotNull("Result service is null", resultService);

		String parentServiceName = resultService.getParentServiceName();
		assertEquals("Parent service name don't equals", "Service # 2", parentServiceName);


	}

	@Test
	public void testAddSomeReadList() {
		// создаем объекты
		Service s1 = new Service();
		Service s2 = new Service();

		s1.setName("Service # 1");
		s2.setName("Service # 2");

		// записываем
		DAOFactory.getInstance().getServiceDAO().addService(s1);
		DAOFactory.getInstance().getServiceDAO().addService(s2);

		// Проверяем чтение листа
		List<Service> resultServiceList = null;

		resultServiceList = DAOFactory.getInstance().getServiceDAO().getAllServices();

		assertNotNull("Readed service list is null", resultServiceList);

		int listSize = resultServiceList.size();
//		assertEquals("Num of items not equals 2", 2, listSize);

		Service resultService1 = resultServiceList.get(listSize-2);
		Service resultService2 = resultServiceList.get(listSize-1);

		assertNotNull("One of services is null", resultService1);
		assertNotNull("One of services is null", resultService2);
				
		String resultService1Name = resultService1.getName();
		assertEquals("First service name not the same", "Service # 1", resultService1Name);

		String resultService2Name = resultService2.getName();
		assertEquals("Second service name not the same", "Service # 2", resultService2Name);
	}

}