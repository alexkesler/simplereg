package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.support.DAOException;

import java.util.List;

public interface GenericDAO <T extends AbstractEntity> extends DAOObservable{
	
	Long addItem(T item) throws DAOException;

	void updateItem(T item) throws DAOException;

	T getItemById(long id) throws DAOException;

	List<T> getAllItems() throws DAOException;

	void removeItem(T item) throws DAOException;
}