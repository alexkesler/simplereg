package org.kesler.simplereg.dao;

import java.util.List;
import java.util.Date;

import org.kesler.simplereg.logic.reception.Reception;

public interface ReceptionDAO extends DAOObservable{
	public void addReception(Reception r);
	public void updateReception(Reception r);
	public Reception getReceptionById(Long id);
	public List getAllReceptions();
	// public List getReceptionsByOpenDate(Date from, Date to);
	public void deleteReception(Reception r);
}