package org.kesler.simplereg.dao;

public interface DAOObservable {
	public void addDAOListener(DAOListener listener);
    public void removeDAOListener(DAOListener listener);
}