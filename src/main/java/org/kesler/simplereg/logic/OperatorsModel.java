package org.kesler.simplereg.logic;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.SQLException;

import org.kesler.simplereg.dao.DAOFactory;

public class OperatorsModel {
	private List<Operator> operators = null;
	private static OperatorsModel instance = null;

	private OperatorsModel() {
	}

	public static synchronized OperatorsModel getInstance() {
		if (instance == null) {
			instance = new OperatorsModel();
		}

		return instance;
	}

	public void readOperators() {
		try {
			operators = DAOFactory.getInstance().getOperatorDAO().getAllOperators();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка чтения из базы данных", JOptionPane.OK_OPTION);			
		}
	}

	public List<Operator> getAllOperators() {
		if (operators == null) {
			readOperators();
		}
		return operators;
	}

	public List<Operator> getActiveOperators() {
		if (operators == null) {
			readOperators();
		}

		ArrayList<Operator> activeOperators = new ArrayList<Operator>();
		for (Operator operator : operators) {
		 	if (operator.getEnabled()) {
		 		activeOperators.add(operator);
		 	}
		 } 

		 return activeOperators;

	}	

	public void addOperator(Operator operator) {
		operators.add(operator);
		try {
			DAOFactory.getInstance().getOperatorDAO().addOperator(operator);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка записи оператора в базу данных", JOptionPane.OK_OPTION);	
			operators.remove(operator);		
		}
	}

	public void saveOperators() {
		try {
			DAOFactory.getInstance().getOperatorDAO().saveOperators(operators);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null,sqle.getMessage(),"Ошибка сохранения операторов в базу данных", JOptionPane.OK_OPTION);	
		}
	}

}