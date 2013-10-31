package org.kesler.simplereg.gui.operators;

import java.util.List;
import javax.swing.JFrame;

import org.kesler.simplereg.gui.GenericListDialogController;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.operator.Operator;


public class OperatorListDialogController implements GenericListDialogController {

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

	}

	public void showDialog(JFrame parentFrame) {
		dialog = new GenericListDialog<Operator>(parentFrame, "Операторы", this);
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
	public void openAddItemDialog() {
		OperatorDialog operatorDialog = new OperatorDialog(dialog);
		operatorDialog.setVisible(true);

		if (operatorDialog.getResult() == OperatorDialog.OK) {
			Operator operator = operatorDialog.getOperator();
			int index = model.addOperator(operator);
			dialog.addedItem(index);
		}

		// Освобождаем ресурсы
		operatorDialog.dispose();
		operatorDialog = null;

	}

	@Override
	public void openEditItemDialog(int index) {
		Operator operator = model.getAllOperators().get(index);
		OperatorDialog operatorDialog = new OperatorDialog(dialog, operator);
		operatorDialog.setVisible(true);

		if (operatorDialog.getResult() == OperatorDialog.OK) {
			model.updateOperator(operator);
			dialog.updatedItem(index);
		}

		// Освобождаем ресурсы
		operatorDialog.dispose();
		operatorDialog = null;

	}

	@Override
	public void removeItem(int index) {
		Operator operator = model.getAllOperators().get(index);
		model.removeOperator(operator);
		dialog.removedItem(index);
	}

	@Override
	public void readItems() {
		model.readOperators();
	}

}