package org.kesler.simplereg.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

public class TestServicesModel {

	@Test
	public void testAddService() {
		ServicesModel model = ServicesModel.getInstance();
		Service service = new Service();
		service.setName("Service # 1");
		model.addService(service);

		List<Service> serviceList = model.getAllServices();
		boolean resultBoolean = serviceList.contains(service);
		assertTrue("Service not in service list", resultBoolean);
	}

}