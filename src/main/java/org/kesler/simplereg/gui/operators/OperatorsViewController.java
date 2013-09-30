package org.kesler.simplereg.gui.operators;

import java.util.List;
import javax.swing.JOptionPane;

import org.kesler.simplereg.logic.operator.Operator;
import org.kesler.simplereg.logic.operator.OperatorsModel;
import org.kesler.simplereg.logic.operator.OperatorsModelListener;

public class OperatorsViewController implements OperatorsModelListener{
	private OperatorsView view;
	private List<Operator> operators;
	private static OperatorsViewController instance = null;

	private OperatorsViewController() {
		view = new OperatorsView(this);
		OperatorsModel.getInstance().addOperatorsModelListener(this);
	}

	public static synchronized OperatorsViewController getInstance() {
		if (instance == null) {
			instance = new OperatorsViewController();
		}

		return instance;
	}

	/**
	* Читает список операторов из базы, открывает окно редактирования
	*/ 
	public void openView() {
		operators = OperatorsModel.getInstance().getAllOperators();
		view.getTableModel().setOperators(operators);
		view.setVisible(true);
	}

	public void operatorsChanged(int status) {
		switch (status) {
			case OperatorsModel.STATUS_UPDATING:
				break;
			case OperatorsModel.STATUS_UPDATED:
				break;
			case OperatorsModel.STATUS_ERROR:
				break;	
			
		}
	}


	public void closeView() {
		boolean changed = false;
		for (Operator operator : operators) {
			if (operator.getState()!=Operator.SAVED_STATE) {
				changed = true;
			}
		}

		int result;
		if (changed) {
			result = JOptionPane.showOptionDialog(null,
												"В список операторов были внесены изменения. Сохранить?",
												"Сохранить?",
												JOptionPane.YES_NO_CANCEL_OPTION,
												JOptionPane.QUESTION_MESSAGE,
												null,
												new String[] {"Сохранить", "Не сохранять", "Отмена"},
												"Сохранить");
			if (result == JOptionPane.YES_OPTION) {
				saveOperators();
			} else if (result == JOptionPane.NO_OPTION) {
				for (Operator operator : operators) {
					operator.setState(Operator.SAVED_STATE);
				}
			} else {
				return ;
			}
		}

		view.setVisible(false);

	}

	/**
	* Сохраняет изменения списка операторов в базу данных
	*/
	public void saveOperators() {
		OperatorsModel.getInstance().saveOperators();
		view.getTableModel().fireTableDataChanged();
	}


	/**
	* Вызывает диалоговое окно со сведениями о новом операторе
	*/
	public void newOperator() {
		Operator newOperator = new Operator();
		newOperator.setState(Operator.NEW_STATE);

		int result = JOptionPane.showOptionDialog(null, 
							view.getOperatorPanel(newOperator), 
							"Добавление оператора",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							new String[] {"Принять", "Отменить"},
							"Принять");

		if (result == JOptionPane.OK_OPTION) {
			view.getOperatorPanel().readOperator();
			operators.add(newOperator);
			view.getTableModel().fireTableDataChanged();
		}

	}

	/**
	* <p>Создает диалоговое окно со сведениями о выбранном операторе,</p> 
	* <p>вызывает функцию view.operatorPanel.readOperator() для сохранения сведений об операторе</p> 
	*/
	public void editOperator(int index) {
		Operator operator = operators.get(index);
		
		int result = JOptionPane.showOptionDialog(null, 
							view.getOperatorPanel(operator), 
							"Добавление оператора",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,
							new String[] {"Принять", "Отменить"},
							"Принять");

		if (result==JOptionPane.OK_OPTION) {
			view.getOperatorPanel().readOperator();
			operator.setState(Operator.EDITED_STATE);
			view.getTableModel().fireTableDataChanged();
		} 

	}

	/**
	* <p>Создает диалоговое окно с запросом подтверждения на удаление оператора</p>
	* <p>Помечает оператора на удаление</p>
	*/
	public void deleteOperator(int index) {
		Operator operator = operators.get(index);

		int result = JOptionPane.showConfirmDialog(null, 
												"Подтверждение удаления",
												"Удалить запись об операторе?",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
		if (result==JOptionPane.OK_OPTION) {
			operator.setState(Operator.DELETED_STATE);
			view.getTableModel().fireTableDataChanged();
			
		}
	}

	
	
}