package org.kesler.simplereg.dao;

import java.util.List;

import org.kesler.simplereg.logic.operator.Operator;

public interface OperatorDAO extends DAOObservable {
	public void saveOperators(List<Operator> os);
	public List getAllOperators();
}