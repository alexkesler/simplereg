package org.kesler.simplereg.dao;

import java.sql.SQLException;
import java.util.List;

import org.kesler.simplereg.logic.Operator;

public interface OperatorDAO {
	public void addOperator(Operator o) throws SQLException;
	public void updateOperator(Operator o) throws SQLException;
	public Operator getOperatorById(Long id) throws SQLException;
	public List getAllOperators() throws SQLException;
	public void deleteOperator(Operator o) throws SQLException;
}