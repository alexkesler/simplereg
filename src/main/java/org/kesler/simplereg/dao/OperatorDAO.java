package org.kesler.simplereg.dao;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Operator;

public interface OperatorDAO {
	public void saveOperators(List<Operator> os) throws SQLException;
	public List getAllOperators() throws SQLException;
}