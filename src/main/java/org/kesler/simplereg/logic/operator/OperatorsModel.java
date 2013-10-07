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
		operators = DAOFactory.getInstance().getOperatorDAO().getAllOperators();
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

	public void saveOperators() {
		DAOFactory.getInstance().getOperatorDAO().saveOperators(operators);
	}

}