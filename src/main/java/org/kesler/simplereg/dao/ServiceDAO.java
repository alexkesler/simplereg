package org.kesler.simplereg.dao;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Service;

public interface ServiceDAO {
	public void addService(Service service) throws SQLException;
	public void updateService(Service service) throws SQLException;
	public Service getServiceById(Long id) throws SQLException;
	public List getAllServices() throws SQLException;
	public void deleteService(Service service) throws SQLException;
}