package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.reception.Reception;

public interface ReceptionDAO {
	public void addReception(Reception r);
	public void updateReception(Reception r);
	public Reception getReceptionById(Long id);
	public List getAllReceptions();
	public void deleteReception(Reception r);
}