package org.kesler.simplereg.gui.operators;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.gui.GenericListDialogController;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.operator.OperatorsModelStateListener;
import org.kesler.simplereg.logic.operator.Operator;


public class OperatorListDialogController implements GenericListDialogController, OperatorsModelStateListener {

	private static OperatorListDialogController instance;

	private OperatorsModel model;
	private GenericListDialog<Operator> dialog;

	public static synchronized OperatorListDialogController getInstance() {
		if (instance == null) {
			instance = new OperatorListDialogController();
		}
		return instance;
	}

	private OperatorListDialogController() {
		model = OperatorsModel.getInstance();
		model.addOperatorsModelStateListener(this);

	}

	public void showDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<Operator>(parentFrame, "Операторы", this);
		List<Operator> operators = model.getAllOperators();
		dialog.setItems(operators);
		dialog.setVisible(true);

		// Освобождаем ресурсы
		dialog.dispose();
		dialog = null;
	}

	@Override
	public List<Operator> getAllItems() {
		return model.getAllOperators();
	}

	@Override
	public boolean openAddItemDialog() {
		boolean result = false;
		OperatorDialog operatorDialog = new OperatorDialog(dialog);
		operatorDialog.setVisible(true);

		if (operatorDialog.getResult() == OperatorDialog.OK) {
			Operator operator = operatorDialog.getOperator();
			int index = model.addOperator(operator);
			dialog.addedItem(index);
			result = true;
		}

		// Освобождаем ресурсы
		operatorDialog.dispose();
		operatorDialog = null;

		return result;
	}

	@Override
	public boolean openEditItemDialog(int index) {
		boolean result = false;
		Operator operator = model.getAllOperators().get(index);
		OperatorDialog operatorDialog = new OperatorDialog(dialog, operator);
		operatorDialog.setVisible(true);

		if (operatorDialog.getResult() == OperatorDialog.OK) {
			model.updateOperator(operator);
			dialog.updatedItem(index);
			result = true;
		}

		// Освобождаем ресурсы
		operatorDialog.dispose();
		operatorDialog = null;

		return result;
	}

	@Override
	public boolean removeItem(int index) {
		boolean result = false;

		Operator operator = model.getAllOperators().get(index);
		
		int confirmResult = JOptionPane.showConfirmDialog(dialog, "Удалить оператора: " + operator + "?", "Удалить?", JOptionPane.YES_NO_OPTION);
		if (confirmResult == JOptionPane.OK_OPTION) { 			
			model.removeOperator(operator);
			dialog.removedItem(index);
			result = true;
		}

		return result;


	}

	@Override
	public void readItems() {
		model.readOperators();
	}

	@Override
	public void operatorsModelStateChanged(ModelState state) {
		
	}

}