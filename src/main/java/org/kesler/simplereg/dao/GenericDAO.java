package org.kesler.simplereg.dao;

import java.util.List;

public interface GenericDAO <T extends AbstractEntity> extends DAOObservable{
	
	public Long addItem(T item);

	public void updateItem(T item);

	public T getItemById(int id);

	public List<T> getAllItems();

	public void removeItem(T item);
}