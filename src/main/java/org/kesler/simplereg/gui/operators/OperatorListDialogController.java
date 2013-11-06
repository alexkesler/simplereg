package org.kesler.simplereg.gui.operators;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.kesler.simplereg.gui.GenericListDialogController;
import org.kesler.simplereg.gui.GenericListDialog;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.operator.OperatorsModelStateListener;
import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.ModelState;

import org.kesler.simplereg.gui.util.InfoDialog;
import org.kesler.simplereg.gui.util.ProcessDialog;

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

	// @Override
	// public List<Operator> getAllItems() {
	// 	return model.getAllOperators();
	// }

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
		model.readOperatorsInSeparateProcess();
	}

	@Override
	public void operatorsModelStateChanged(ModelState state) {
		switch (state) {
			case CONNECTING:
				ProcessDialog.showProcess(dialog, "Соединяюсь...");			
			break;
			case READING:
				ProcessDialog.showProcess(dialog, "Читаю список операторов");
			break;	
			case WRITING:
				ProcessDialog.showProcess(dialog, "Сохраняю изменения");
			break;	
			case UPDATED:
				dialog.setItems(model.getAllOperators());
				ProcessDialog.hideProcess();
				new InfoDialog(dialog, "Обновлено", 500, InfoDialog.GREEN).showInfo();	
			break;
			case READY:
				ProcessDialog.hideProcess();	
			break;
			case ERROR:				
				ProcessDialog.hideProcess();
				new InfoDialog(dialog, "Ошибка базы данных", 1000, InfoDialog.RED).showInfo();
			break;			
		}
		
	}

}