package org.kesler.simplereg.logic.operator;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.SQLException;

import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;

public class OperatorsModel implements DAOListener{

	private List<Operator> operators = null;
	private static OperatorsModel instance = null;

	private List<OperatorsModelStateListener> listeners;

	private OperatorsModel() {
		listeners = new ArrayList<OperatorsModelStateListener>();
		DAOFactory.getInstance().getOperatorDAO().addDAOListener(this);
	}

	public static synchronized OperatorsModel getInstance() {
		if (instance == null) {
			instance = new OperatorsModel();
		}
		return instance;
	}

	public void addOperatorsModelStateListener(OperatorsModelStateListener listener) {
		listeners.add(listener);
	}

	public void readOperators() {
		operators = DAOFactory.getInstance().getOperatorDAO().getAllItems();
		checkAdmin();
		notifyListeners(OperatorsModelState.UPDATED);

	}

	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
			notifyListeners(OperatorsModelState.CONNECTING);
			break;
			case READING:
			notifyListeners(OperatorsModelState.READING);
			break;
			case READY:
			// ничего не делаем			
			break;
			case ERROR:
			notifyListeners(OperatorsModelState.ERROR);
		}
	}

	private void notifyListeners(OperatorsModelState state) {
		for (OperatorsModelStateListener listener : listeners) {
			listener.operatorsModelStateChanged(state);			
		}
	}

	public List<Operator> getAllOperators() {
		if (operators == null) {
			readOperators();
		}
		return operators;
	}

	public int addOperator(Operator operator) {
		if (operators == null) {
			readOperators();
		}
		DAOFactory.getInstance().getOperatorDAO().addItem(operator);
		operators.add(operator);
		int index = operators.size()-1;
		return index;
	}

	public void updateOperator(Operator operator) {
		DAOFactory.getInstance().getOperatorDAO().updateItem(operator);
	}

	public void removeOperator(Operator operator) {
		DAOFactory.getInstance().getOperatorDAO().removeItem(operator);
		operators.remove(operator);
	}

	public List<Operator> getActiveOperators() {
		if (operators == null) {
			readOperators();
		}

		ArrayList<Operator> activeOperators = new ArrayList<Operator>();
		for (Operator operator : operators) {
		 	if (operator.isEnabled()) {
		 		activeOperators.add(operator);
		 	}
		 } 
		 return activeOperators;
	}	

	/**
	* Функция проверяет наличие оператора с административными правами, если не находит его - добавляет
	*/
	private void checkAdmin() {
		if (operators == null) {
			return;
		}

		boolean adminExist = false;
		for (Operator operator: operators) {
			if (operator.isAdmin()) adminExist = true;
		}

		if (!adminExist) {
			Operator admin = new Operator();
			admin.setSurName("Администратор по умолчанию");
			admin.setControler(true);
			admin.setAdmin(true);
			admin.setEnabled(true);
			admin.setPassword("");

			// добавить запись оператора в БД
			addOperator(admin);
		}

	}

}