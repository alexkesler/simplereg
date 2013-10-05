package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.operator.Operator;

public interface OperatorDAO {
	public void saveOperators(List<Operator> os);
	public List getAllOperators();
	public void addDAOListener(DAOListener listener);
}