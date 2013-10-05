package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.Service;

public interface ServiceDAO {
	public void addService(Service service);
	public void updateService(Service service);
	public Service getServiceById(Long id);
	public List<Service> getAllServices();
	public List<Service> getActiveServices();
	public void deleteService(Service service);
	public void saveServices(List<Service> services);

}