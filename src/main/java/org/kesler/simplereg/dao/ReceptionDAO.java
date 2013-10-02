package org.kesler.simplereg.dao;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.reception.Reception;

public interface ReceptionDAO {
	public void addReception(Reception r) throws SQLException;
	public void updateReception(Reception r) throws SQLException;
	public Reception getReceptionById(Long id) throws SQLException;
	public List getAllReceptions() throws SQLException;
	public void deleteReception(Reception r) throws SQLException;
}