package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.Service;

public interface ServiceDAO {
	public void addService(Service service);
	public void updateService(Service service);
	public Service getServiceById(Long id);
	public List getAllServices();
	public void deleteService(Service service);
}