package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.Service;

public interface ServiceDAO extends GenericDAO<Service> {
	// добавляем метод, специфичный для услуг
	public List<Service> getActiveServices();
}