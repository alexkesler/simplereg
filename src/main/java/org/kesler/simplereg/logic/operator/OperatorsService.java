package org.kesler.simplereg.logic.operator;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.dao.DAOFactory;
import org.kesler.simplereg.dao.DAOListener;
import org.kesler.simplereg.dao.DAOState;
import org.kesler.simplereg.logic.ServiceState;

public class OperatorsService implements DAOListener{

	private List<Operator> operators = null;
	private static OperatorsService instance = null;

	private List<OperatorsServiceStateListener> listeners;

	private OperatorsService() {
		listeners = new ArrayList<OperatorsServiceStateListener>();
		DAOFactory.getInstance().getOperatorDAO().addDAOListener(this);
	}

	public static synchronized OperatorsService getInstance() {
		if (instance == null) {
			instance = new OperatorsService();
		}
		return instance;
	}

	public void addOperatorsServiceStateListener(OperatorsServiceStateListener listener) {
		listeners.add(listener);
	}
	public void removeOperatorsServiceStateListener(OperatorsServiceStateListener listener) {
		listeners.remove(listener);
	}



	public void readOperators() {
		operators = DAOFactory.getInstance().getOperatorDAO().getAllItems();
		checkAdmin();
		notifyListeners(ServiceState.UPDATED);

	}

	/**
	* Читает операторов в отдельном потоке
	*/
	public void readOperatorsInSeparateThread() {
		Thread readOperatorsThread = new Thread(new Runnable() {
			public void run() {
				readOperators();
			}
		});
		readOperatorsThread.start();
	}

	@Override
	public void daoStateChanged(DAOState state) {
		switch (state) {
			case CONNECTING:
				notifyListeners(ServiceState.CONNECTING);
			break;

			case READING:
				notifyListeners(ServiceState.READING);
			break;

			case WRITING:
				notifyListeners(ServiceState.WRITING);
			break;

			case READY:
				notifyListeners(ServiceState.READY);
			break;

			case ERROR:
				notifyListeners(ServiceState.ERROR);
			break;
		}
	}

	private void notifyListeners(ServiceState state) {
		for (OperatorsServiceStateListener listener : listeners) {
			listener.operatorsServiceStateChanged(state);
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
			admin.setSurName("По умолчанию");
			admin.setFirstName("Админ");
			admin.setControler(true);
			admin.setAdmin(true);
			admin.setEnabled(true);
			admin.setPassword("");

			// добавить запись оператора в БД
			addOperator(admin);
		}

	}

}