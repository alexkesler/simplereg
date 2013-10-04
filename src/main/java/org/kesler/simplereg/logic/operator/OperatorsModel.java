package org.kesler.simplereg.logic.operator;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.sql.SQLException;

import org.kesler.simplereg.dao.DAOFactory;

public class OperatorsModel {


	public static final int STATUS_UPDATED = 0;
	public static final int STATUS_UPDATING = 1;
	public static final int STATUS_ERROR = -1;

	private List<Operator> operators = null;
	private static OperatorsModel instance = null;
	private Thread readerThread;

	private List<OperatorsModelListener> listeners;

	private OperatorsModel() {
		listeners = new ArrayList<OperatorsModelListener>();
	}

	public static synchronized OperatorsModel getInstance() {
		if (instance == null) {
			instance = new OperatorsModel();
		}
		return instance;
	}

	public void addOperatorsModelListener(OperatorsModelListener listener) {
		listeners.add(listener);
	}

	public void readOperators() {
		readerThread = new Thread(new Reader()); 
		notifyListeners(STATUS_UPDATING);
		readerThread.start();

	}

	public void stopReadOperators() {
		readerThread.interrupt();
	}

	private void notifyListeners(int status) {
		for (OperatorsModelListener listener : listeners) {
			listener.operatorsChanged(status);			
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

	class Reader implements Runnable {
		public void run() {
			operators = DAOFactory.getInstance().getOperatorDAO().getAllOperators();
			notifyListeners(STATUS_UPDATED);
		}		
	}

}