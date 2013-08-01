package org.kesler.simplereg.gui.operators;

import java.util.List;

import org.kesler.simplereg.logic.Operator;
import org.kesler.simplereg.logic.OperatorsModel;

public class OperatorsViewController {
	private OperatorsView view;
	private List<Operator> operators;
	private static OperatorsViewController instance = null;

	private OperatorsViewController() {
		view = new OperatorsView(this);
	}

	public static synchronized OperatorsViewController getInstance() {
		if (instance == null) {
			instance = new OperatorsViewController();
		}

		return instance;
	}

	public void openView() {
		operators = OperatorsModel.getInstance().getAllOperators();
		view.setOperators(operators);
		view.setVisible(true);
	}

	public void saveOperators() {
		OperatorsModel.getInstance().saveOperators();
	}

	
	
}