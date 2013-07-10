package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;


public class ServicesModel {
	private static ServicesModel instance;
	private List<Service> services;

	private ServicesModel() {
		services = new ArrayList<Service>();
	}

	public static synchronized ServicesModel getInstance() {
		if (instance == null) {
			instance = new ServicesModel();
		}
		return instance;
	}

	public List<Service> getAllServices() {
		return services;
	}

	public void addService(Service service) {
		services.add(service);
	}

}