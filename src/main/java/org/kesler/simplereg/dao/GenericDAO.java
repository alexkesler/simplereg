package org.kesler.simplereg.dao;

import org.kesler.simplereg.dao.support.DAOException;

import java.util.List;

public interface GenericDAO <T extends AbstractEntity> extends DAOObservable{
	
	public Long addItem(T item) throws DAOException;

	public void updateItem(T item) throws DAOException;

	public T getItemById(long id) throws DAOException;

	public List<T> getAllItems() throws DAOException;

	public void removeItem(T item) throws DAOException;
}