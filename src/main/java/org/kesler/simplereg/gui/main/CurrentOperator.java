package org.kesler.simplereg.gui.main;

import java.util.List;
import java.util.ArrayList;

import org.kesler.simplereg.logic.Operator;

public class CurrentOperator {
	private static CurrentOperator instance;
	private Operator operator;

	private List<CurrentOperatorListener> listeners;

	private CurrentOperator() {
		operator = null;
		listeners = new ArrayList<CurrentOperatorListener>();
	}

	public void setOperator(Operator o) {
		operator = o;
		notifyListeners();
	}

	public Operator getOperator() {
		return operator;
	} 

	public void resetOperator() {
		operator = null;
		notifyListeners();
	}


	public static synchronized CurrentOperator getInstance() {
		if (instance == null) {
			instance = new CurrentOperator();
		}
		return instance;
	}

	public void addCurrentOperatorListener(CurrentOperatorListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners() {
		for (CurrentOperatorListener listener: listeners) {
			listener.currentOperatorChanged(operator);
		}
	}



}