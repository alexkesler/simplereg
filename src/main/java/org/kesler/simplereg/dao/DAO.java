package org.kesler.simplereg.dao;

import java.util.List;

public interface DAO <T extends AbstractEntity> {
	
	public Long add(T o);

	public void update(T o);

	public T getById(int id);

	public List<T> getAll();

	public void remove(T o);
}