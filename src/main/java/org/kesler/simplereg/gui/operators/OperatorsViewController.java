package org.kesler.simplereg.gui.operators;

import java.util.List;
import javax.swing.JOptionPane;

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
		view.getTableModel().setOperators(operators);
		view.setVisible(true);
	}

	public void saveOperators() {
		OperatorsModel.getInstance().saveOperators();
	}

	public void newOperator() {
		Operator newOperator = new Operator();

		int result = JOptionPane.showOptionDialog(null, 
											view.getOperatorPanel(newOperator), 
											"Добавление оператора",
											JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.QUESTION_MESSAGE,
											null,
											new String[] {"Принять", "Отменить"},
											"Принять");

		if (result==JOptionPane.OK_OPTION) {
			operators.add(newOperator);
			view.getTableModel().fireTableDataChanged();
		}

	}

	
	
}